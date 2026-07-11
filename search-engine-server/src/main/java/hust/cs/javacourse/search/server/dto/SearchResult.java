package hust.cs.javacourse.search.server.dto;

import java.util.Map;

/**
 * 单条搜索结果
 */
public class SearchResult {

    private int docId;
    private String docName;
    private String docPath;
    private double score;
    /** 命中词 → 词频 */
    private Map<String, Integer> termFreqMap;
    /** 带高亮 <mark> 标签的文本片段 */
    private String snippet;

    public SearchResult() {
    }

    public SearchResult(int docId, String docName, String docPath, double score,
            Map<String, Integer> termFreqMap, String snippet) {
        this.docId = docId;
        this.docName = docName;
        this.docPath = docPath;
        this.score = score;
        this.termFreqMap = termFreqMap;
        this.snippet = snippet;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Map<String, Integer> getTermFreqMap() {
        return termFreqMap;
    }

    public void setTermFreqMap(Map<String, Integer> termFreqMap) {
        this.termFreqMap = termFreqMap;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
