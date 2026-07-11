package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

/**
 * 基于正则表达式的过滤器
 */
public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    protected String pattern = Config.TERM_FILTER_PATTERN;

    public PatternTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        while (true) {
            AbstractTermTuple tuple = input.next();
            if (tuple == null) {
                return null;
            }
            if (tuple.term.getContent().matches(this.pattern)) {
                return tuple;
            }
        }
    }
}
