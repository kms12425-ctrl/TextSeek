package hust.cs.javacourse.search.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 搜索引擎配置属性 — 映射 application.yml 中 search-engine.* 配置
 */
@Component
@ConfigurationProperties(prefix = "search-engine")
public class SearchEngineConfig {

    /** 文档存储目录 */
    private String documentDir = "./data/documents/";

    /** 索引存储目录 */
    private String indexDir = "./data/index/";

    /** 索引文件名 */
    private String indexFile = "index.dat";

    /** 上传文件最大大小 */
    private String uploadMaxSize = "10MB";

    /** 是否忽略大小写 */
    private boolean ignoreCase = true;

    /** 单词过滤正则 */
    private String termFilterPattern = "[a-zA-Z]+";

    /** 最小单词长度 */
    private int termMinLength = 3;

    /** 最大单词长度 */
    private int termMaxLength = 20;

    // ── Getters & Setters ──

    public String getDocumentDir() {
        return documentDir;
    }

    public void setDocumentDir(String documentDir) {
        this.documentDir = documentDir;
    }

    public String getIndexDir() {
        return indexDir;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public String getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(String indexFile) {
        this.indexFile = indexFile;
    }

    public String getUploadMaxSize() {
        return uploadMaxSize;
    }

    public void setUploadMaxSize(String uploadMaxSize) {
        this.uploadMaxSize = uploadMaxSize;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public String getTermFilterPattern() {
        return termFilterPattern;
    }

    public void setTermFilterPattern(String termFilterPattern) {
        this.termFilterPattern = termFilterPattern;
    }

    public int getTermMinLength() {
        return termMinLength;
    }

    public void setTermMinLength(int termMinLength) {
        this.termMinLength = termMinLength;
    }

    public int getTermMaxLength() {
        return termMaxLength;
    }

    public void setTermMaxLength(int termMaxLength) {
        this.termMaxLength = termMaxLength;
    }

    /** 索引文件完整路径 */
    public String getIndexPath() {
        if (!indexDir.endsWith("/") && !indexDir.endsWith("\\")) {
            return indexDir + "/" + indexFile;
        }
        return indexDir + indexFile;
    }
}
