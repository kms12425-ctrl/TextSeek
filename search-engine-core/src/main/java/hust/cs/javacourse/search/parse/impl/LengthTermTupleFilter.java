package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

/**
 * 基于单词长度的过滤器
 */
public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    protected int minLength = Config.TERM_FILTER_MINLENGTH;
    protected int maxLength = Config.TERM_FILTER_MAXLENGTH;

    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        while (true) {
            AbstractTermTuple tuple = input.next();
            if (tuple == null) {
                return null;
            }

            int length = tuple.term.getContent().length();
            if (length >= this.minLength && length <= this.maxLength) {
                return tuple;
            }
        }
    }
}
