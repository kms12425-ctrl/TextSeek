package hust.cs.javacourse.search.server.dto;

/**
 * 搜索结果分页响应
 */
public class SearchResponse {

    private int totalHits;
    private int page;
    private int size;
    private java.util.List<SearchResult> hits;

    public SearchResponse() {
        this.hits = new java.util.ArrayList<>();
    }

    public SearchResponse(int totalHits, int page, int size, java.util.List<SearchResult> hits) {
        this.totalHits = totalHits;
        this.page = page;
        this.size = size;
        this.hits = hits;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public java.util.List<SearchResult> getHits() {
        return hits;
    }

    public void setHits(java.util.List<SearchResult> hits) {
        this.hits = hits;
    }
}
