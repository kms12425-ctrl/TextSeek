package hust.cs.javacourse.search.server.service;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.server.config.SearchEngineConfig;
import hust.cs.javacourse.search.server.dto.DocInfo;
import hust.cs.javacourse.search.server.dto.SearchResponse;
import hust.cs.javacourse.search.server.dto.SearchResult;
import hust.cs.javacourse.search.util.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 搜索引擎核心服务 — 封装索引构建、文档管理和搜索功能
 */
@Service
public class IndexService {

    private final SearchEngineConfig config;

    private AbstractIndex index;
    private int nextDocId = 0;
    /** 存储 docId → 原始文件名 的映射（index 里存的是绝对路径） */
    private final Map<Integer, String> docIdToFileName = new TreeMap<>();

    public IndexService(SearchEngineConfig config) {
        this.config = config;
    }

    /**
     * 服务启动时自动加载已有索引（若存在），否则初始化空索引
     */
    @PostConstruct
    public void init() {
        // 同步配置到 core 模块
        Config.IGNORE_CASE = config.isIgnoreCase();
        Config.TERM_FILTER_PATTERN = config.getTermFilterPattern();
        Config.TERM_FILTER_MINLENGTH = config.getTermMinLength();
        Config.TERM_FILTER_MAXLENGTH = config.getTermMaxLength();

        File indexFile = new File(config.getIndexPath());
        if (indexFile.exists()) {
            index = new Index();
            index.load(indexFile);
            // 恢复 nextDocId
            for (Integer id : index.docIdToDocPathMapping.keySet()) {
                if (id >= nextDocId)
                    nextDocId = id + 1;
            }
            System.out.println("[IndexService] 已加载索引: " + index.getDictionary().size() + " 个词条");
        } else {
            index = new Index();
            System.out.println("[IndexService] 初始化空索引");
        }
    }

    // ═══════════════════ 文档管理 ═══════════════════

    /**
     * 上传并索引一份文档
     */
    public DocInfo addDocument(MultipartFile file) throws IOException {
        // 确保文档目录存在
        File docDir = new File(config.getDocumentDir());
        if (!docDir.exists())
            docDir.mkdirs();

        // 保存文件
        String originalName = file.getOriginalFilename();
        String savedName = (originalName != null) ? originalName : "document_" + nextDocId + ".txt";
        Path targetPath = Paths.get(config.getDocumentDir(), savedName);
        Files.write(targetPath, file.getBytes());

        // 解析文档
        AbstractDocumentBuilder docBuilder = new DocumentBuilder();
        AbstractDocument document = docBuilder.build(nextDocId, targetPath.toString(), targetPath.toFile());

        // 加入索引
        index.addDocument(document);
        docIdToFileName.put(nextDocId, savedName);

        int docId = nextDocId;
        nextDocId++;

        // 优化并保存
        index.optimize();
        saveIndex();

        return new DocInfo(docId, savedName, targetPath.toString(), document.getTupleSize());
    }

    /**
     * 删除文档并重建索引
     */
    public void deleteDocument(int docId) {
        // 删除物理文件
        String docPath = index.getDocName(docId);
        if (docPath != null) {
            try {
                Files.deleteIfExists(Paths.get(docPath));
            } catch (IOException ignored) {
            }
        }

        // 重新扫描文档目录重建索引
        rebuildIndex();
    }

    /**
     * 列出所有已索引文档
     */
    public List<DocInfo> listDocuments() {
        List<DocInfo> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : index.docIdToDocPathMapping.entrySet()) {
            int docId = entry.getKey();
            String path = entry.getValue();
            String name = new File(path).getName();

            // 统计 term 数量
            int termCount = 0;
            for (AbstractPostingList pl : index.termToPostingListMapping.values()) {
                if (pl.indexOf(docId) != -1)
                    termCount++;
            }

            list.add(new DocInfo(docId, name, path, termCount));
        }
        return list;
    }

    // ═══════════════════ 搜索 ═══════════════════

    /**
     * 单关键词搜索（分页）
     */
    public SearchResponse search(String query, int page, int size) {
        IndexSearcher searcher = new IndexSearcher();
        searcher.setIndex(index);

        Sort sorter = new SimpleSorter();
        AbstractHit[] hits = searcher.search(new Term(query), sorter);

        return buildResponse(hits, page, size, query);
    }

    /**
     * 双关键词搜索（AND/OR，分页）
     */
    public SearchResponse search(String query1, String query2, String mode, int page, int size) {
        IndexSearcher searcher = new IndexSearcher();
        searcher.setIndex(index);

        AbstractIndexSearcher.LogicalCombination combine = "and".equalsIgnoreCase(mode)
                ? AbstractIndexSearcher.LogicalCombination.AND
                : AbstractIndexSearcher.LogicalCombination.OR;

        Sort sorter = new SimpleSorter();
        AbstractHit[] hits = searcher.search(new Term(query1), new Term(query2), sorter, combine);

        return buildResponse(hits, page, size, query1 + " " + query2);
    }

    // ═══════════════════ 索引管理 ═══════════════════

    /**
     * 全量重建索引
     */
    public void rebuildIndex() {
        AbstractDocumentBuilder docBuilder = new DocumentBuilder();
        AbstractIndexBuilder indexBuilder = new IndexBuilder(docBuilder);

        index = indexBuilder.buildIndex(config.getDocumentDir());
        nextDocId = index.docIdToDocPathMapping.size();

        saveIndex();
        System.out.println("[IndexService] 索引重建完成: " + index.getDictionary().size() + " 个词条");
    }

    /**
     * 保存索引到文件
     */
    private void saveIndex() {
        File indexDir = new File(config.getIndexDir());
        if (!indexDir.exists())
            indexDir.mkdirs();
        index.save(new File(config.getIndexPath()));
    }

    /**
     * 返回索引统计信息
     */
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("totalDocs", index.docIdToDocPathMapping.size());
        status.put("totalTerms", index.getDictionary().size());
        status.put("indexFile", config.getIndexPath());
        return status;
    }

    // ═══════════════════ 内部工具 ═══════════════════

    private SearchResponse buildResponse(AbstractHit[] hits, int page, int size, String query) {
        List<SearchResult> results = new ArrayList<>();

        // 分页
        int start = page * size;
        int end = Math.min(start + size, hits.length);

        for (int i = start; i < end; i++) {
            AbstractHit hit = hits[i];
            Map<String, Integer> freqMap = new LinkedHashMap<>();
            for (Map.Entry<AbstractTerm, AbstractPosting> e : hit.getTermPostingMapping().entrySet()) {
                freqMap.put(e.getKey().getContent(), e.getValue().getFreq());
            }

            String snippet = buildSnippet(hit.getContent(), query, 80);

            String path = hit.getDocPath();
            String name = (path != null) ? new File(path).getName() : "unknown";

            results.add(new SearchResult(hit.getDocId(), name, path, hit.getScore(), freqMap, snippet));
        }

        return new SearchResponse(hits.length, page, size, results);
    }

    /**
     * 生成包含命中关键词上下文的文本片段
     */
    private String buildSnippet(String content, String query, int contextLen) {
        if (content == null || content.isEmpty())
            return "";
        String lowerContent = content.toLowerCase();
        String lowerQuery = query.toLowerCase();

        int pos = lowerContent.indexOf(lowerQuery);
        if (pos == -1) {
            // 没找到精确匹配，返回开头
            return content.substring(0, Math.min(contextLen * 2, content.length()));
        }

        int start = Math.max(0, pos - contextLen);
        int end = Math.min(content.length(), pos + query.length() + contextLen);

        StringBuilder sb = new StringBuilder();
        if (start > 0)
            sb.append("...");
        sb.append(content, start, end);
        if (end < content.length())
            sb.append("...");

        // 高亮关键词
        String snippet = sb.toString();
        snippet = snippet.replaceAll("(?i)(" + java.util.regex.Pattern.quote(query) + ")", "<mark>$1</mark>");
        return snippet;
    }
}
