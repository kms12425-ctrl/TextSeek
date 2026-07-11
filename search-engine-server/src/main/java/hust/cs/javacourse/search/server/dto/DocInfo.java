package hust.cs.javacourse.search.server.dto;

/**
 * 文档信息摘要
 */
public class DocInfo {

    private int docId;
    private String docName;
    private String docPath;
    private int termCount;

    public DocInfo() {
    }

    public DocInfo(int docId, String docName, String docPath, int termCount) {
        this.docId = docId;
        this.docName = docName;
        this.docPath = docPath;
        this.termCount = termCount;
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

    public int getTermCount() {
        return termCount;
    }

    public void setTermCount(int termCount) {
        this.termCount = termCount;
    }
}
