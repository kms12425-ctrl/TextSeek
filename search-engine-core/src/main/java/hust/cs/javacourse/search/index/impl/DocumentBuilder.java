package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * AbstractDocumentBuilder的具体实现类
 */
public class DocumentBuilder extends AbstractDocumentBuilder {
    @Override
    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream) {
        Document document = new Document();
        document.setDocId(docId);
        document.setDocPath(docPath);
        AbstractTermTuple tuple;
        while ((tuple = termTupleStream.next()) != null) {
            document.addTuple(tuple);
        }
        termTupleStream.close();
        return document;
    }

    @Override
    public AbstractDocument build(int docId, String docPath, File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            AbstractTermTupleStream stream = new TermTupleScanner(reader);
            stream = new PatternTermTupleFilter(stream);
            stream = new StopWordTermTupleFilter(stream);
            stream = new LengthTermTupleFilter(stream);
            return build(docId, docPath, stream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
