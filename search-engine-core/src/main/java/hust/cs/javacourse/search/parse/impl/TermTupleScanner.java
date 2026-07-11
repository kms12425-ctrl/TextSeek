package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * AbstractTermTupleScanner的具体实现类
 */
public class TermTupleScanner extends AbstractTermTupleScanner {
    private String[] terms = null;
    private int index = 0;
    private int curPos = 0;

    public TermTupleScanner() {
        super();
    }

    public TermTupleScanner(BufferedReader input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        try {
            while (true) {
                if (terms != null && index < terms.length) {
                    String termContent = terms[index++];
                    if (termContent == null || termContent.length() == 0) {
                        continue;
                    }

                    if (Config.IGNORE_CASE) {
                        termContent = termContent.toLowerCase();
                    }
                    TermTuple tuple = new TermTuple();
                    Term term = new Term();
                    term.setContent(termContent);
                    tuple.term = term;
                    tuple.curPos = curPos++;
                    return tuple;
                }

                String line = input.readLine();
                if (line == null) {
                    return null;
                }

                terms = line.split(Config.STRING_SPLITTER_REGEX);
                index = 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
