package hust.cs.javacourse.search.server.service;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.server.config.SearchEngineConfig;
import hust.cs.javacourse.search.server.dto.DocInfo;
import hust.cs.javacourse.search.server.dto.DocumentDetail;
import hust.cs.javacourse.search.server.dto.SearchResponse;
import hust.cs.javacourse.search.server.dto.SearchResult;
import hust.cs.javacourse.search.util.Config;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class IndexService {

    private final SearchEngineConfig config;

    private AbstractIndex index;
    private int nextDocId = 0;

    public IndexService(SearchEngineConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void init() {
        Config.IGNORE_CASE = config.isIgnoreCase();
        Config.TERM_FILTER_PATTERN = config.getTermFilterPattern();
        Config.TERM_FILTER_MINLENGTH = config.getTermMinLength();
        Config.TERM_FILTER_MAXLENGTH = config.getTermMaxLength();

        File indexFile = new File(config.getIndexPath());
        if (indexFile.exists()) {
            index = new Index();
            index.load(indexFile);
            nextDocId = nextAvailableDocId();
        } else {
            index = new Index();
        }
    }

    public DocInfo addDocument(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(config.getDocumentDir()));

        String originalName = file.getOriginalFilename();
        String savedName = originalName != null && !originalName.isBlank()
                ? originalName
                : "document_" + nextDocId + ".txt";

        Path targetPath = Paths.get(config.getDocumentDir(), savedName);
        Files.write(targetPath, file.getBytes());

        AbstractDocumentBuilder documentBuilder = new DocumentBuilder();
        AbstractDocument document = documentBuilder.build(nextDocId, targetPath.toString(), targetPath.toFile());

        index.addDocument(document);
        int docId = nextDocId;
        nextDocId++;

        index.optimize();
        saveIndex();

        return toDocInfo(docId, targetPath.toString());
    }

    public void deleteDocument(int docId) {
        String docPath = index.getDocName(docId);
        if (docPath != null) {
            try {
                Files.deleteIfExists(Paths.get(docPath));
            } catch (IOException ignored) {
            }
        }
        rebuildIndex();
    }

    public List<DocInfo> listDocuments() {
        List<DocInfo> documents = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : index.docIdToDocPathMapping.entrySet()) {
            documents.add(toDocInfo(entry.getKey(), entry.getValue()));
        }
        return documents;
    }

    public DocumentDetail getDocument(int docId) throws IOException {
        String docPath = index.getDocName(docId);
        if (docPath == null) {
            return null;
        }

        DocInfo docInfo = toDocInfo(docId, docPath);
        String content = stripUtf8Bom(Files.readString(Paths.get(docPath), StandardCharsets.UTF_8));
        return new DocumentDetail(
                docInfo.getDocId(),
                docInfo.getDocName(),
                docInfo.getDocPath(),
                docInfo.getTermCount(),
                content);
    }

    public SearchResponse search(String query, int page, int size) {
        IndexSearcher searcher = new IndexSearcher();
        searcher.setIndex(index);

        Sort sorter = new SimpleSorter();
        AbstractHit[] hits = searcher.search(new Term(query), sorter);
        return buildResponse(hits, page, size);
    }

    public SearchResponse search(String query1, String query2, String mode, int page, int size) {
        IndexSearcher searcher = new IndexSearcher();
        searcher.setIndex(index);

        AbstractIndexSearcher.LogicalCombination combination = "and".equalsIgnoreCase(mode)
                ? AbstractIndexSearcher.LogicalCombination.AND
                : AbstractIndexSearcher.LogicalCombination.OR;

        Sort sorter = new SimpleSorter();
        AbstractHit[] hits = searcher.search(new Term(query1), new Term(query2), sorter, combination);
        return buildResponse(hits, page, size);
    }

    public void rebuildIndex() {
        AbstractDocumentBuilder documentBuilder = new DocumentBuilder();
        AbstractIndexBuilder indexBuilder = new IndexBuilder(documentBuilder);

        index = indexBuilder.buildIndex(config.getDocumentDir());
        nextDocId = nextAvailableDocId();
        saveIndex();
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("totalDocs", index.docIdToDocPathMapping.size());
        status.put("totalTerms", index.getDictionary().size());
        status.put("indexFile", config.getIndexPath());
        return status;
    }

    private void saveIndex() {
        File indexDir = new File(config.getIndexDir());
        if (!indexDir.exists()) {
            indexDir.mkdirs();
        }
        index.save(new File(config.getIndexPath()));
    }

    private int nextAvailableDocId() {
        return index.docIdToDocPathMapping.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(-1) + 1;
    }

    private SearchResponse buildResponse(AbstractHit[] hits, int page, int size) {
        List<SearchResult> results = new ArrayList<>();
        int start = page * size;
        int end = Math.min(start + size, hits.length);

        for (int i = start; i < end; i++) {
            AbstractHit hit = hits[i];
            Map<String, Integer> termFreqMap = new LinkedHashMap<>();
            for (Map.Entry<AbstractTerm, AbstractPosting> entry : hit.getTermPostingMapping().entrySet()) {
                termFreqMap.put(entry.getKey().getContent(), entry.getValue().getFreq());
            }

            String path = hit.getDocPath();
            String name = path != null ? new File(path).getName() : "unknown";
            String snippet = buildSnippet(hit.getContent(), hit.getTermPostingMapping().keySet(), 80);

            results.add(new SearchResult(hit.getDocId(), name, path, hit.getScore(), termFreqMap, snippet));
        }

        return new SearchResponse(hits.length, page, size, results);
    }

    private String buildSnippet(String content, Set<AbstractTerm> terms, int contextLength) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        String lowerContent = content.toLowerCase();
        int matchPos = -1;
        int matchLength = 0;

        for (AbstractTerm term : terms) {
            String candidate = term.getContent().toLowerCase();
            int candidatePos = lowerContent.indexOf(candidate);
            if (candidatePos != -1 && (matchPos == -1 || candidatePos < matchPos)) {
                matchPos = candidatePos;
                matchLength = candidate.length();
            }
        }

        if (matchPos == -1) {
            return content.substring(0, Math.min(contextLength * 2, content.length()));
        }

        int start = Math.max(0, matchPos - contextLength);
        int end = Math.min(content.length(), matchPos + matchLength + contextLength);

        StringBuilder snippet = new StringBuilder();
        if (start > 0) {
            snippet.append("...");
        }
        snippet.append(content, start, end);
        if (end < content.length()) {
            snippet.append("...");
        }
        return snippet.toString();
    }

    private DocInfo toDocInfo(int docId, String path) {
        String name = new File(path).getName();
        int termCount = 0;
        for (AbstractPostingList postingList : index.termToPostingListMapping.values()) {
            if (postingList.indexOf(docId) != -1) {
                termCount++;
            }
        }
        return new DocInfo(docId, name, path, termCount);
    }

    private String stripUtf8Bom(String content) {
        if (content != null && !content.isEmpty() && content.charAt(0) == '\uFEFF') {
            return content.substring(1);
        }
        return content;
    }
}
