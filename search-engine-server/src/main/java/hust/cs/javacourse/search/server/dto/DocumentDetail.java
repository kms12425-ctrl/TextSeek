package hust.cs.javacourse.search.server.dto;

/**
 * Detailed document payload for the document detail page.
 */
public class DocumentDetail extends DocInfo {

    private String content;

    public DocumentDetail() {
    }

    public DocumentDetail(int docId, String docName, String docPath, int termCount, String content) {
        super(docId, docName, docPath, termCount);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
