package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractTermTuple;

import java.util.List;

/**
 * AbstractDocument的具体实现类
 */
public class Document extends AbstractDocument {
    public Document() {
        super();
    }

    public Document(int docId, java.lang.String docPath) {
        super(docId, docPath);
    }

    public Document(int docId, java.lang.String docPath, java.util.List<AbstractTermTuple> tuples) {
        super(docId, docPath, tuples);
    }

    @Override
    public int getDocId() {
        return this.docId;
    }

    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    @Override
    public String getDocPath() {
        return docPath;
    }

    @Override
    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    @Override
    public List<AbstractTermTuple> getTuples() {
        return this.tuples;
    }

    @Override
    public void addTuple(AbstractTermTuple tuple) {
        this.tuples.add(tuple);
    }

    @Override
    public boolean contains(AbstractTermTuple tuple) {
        return (this.tuples.contains(tuple));
    }

    @Override
    public AbstractTermTuple getTuple(int index) {
        return this.tuples.get(index);
    }

    @Override
    public int getTupleSize() {
        return this.tuples.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("docID: " + this.getDocId() + "\n");
        sb.append("docPath: " + this.getDocPath() + "\n");
        sb.append("tuples: \n");
        for (AbstractTermTuple tmp : this.tuples) {
            sb.append(tmp.toString());
        }
        return sb.toString();
    }
}
