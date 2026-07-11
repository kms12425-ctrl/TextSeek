package hust.cs.javacourse.search.index.impl;

import java.util.Objects;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTermTuple;

/**
 * AbstractTermTuple的具体实现类
 */
public class TermTuple extends AbstractTermTuple {
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AbstractTermTuple))
            return false;
        AbstractTermTuple tmp = (AbstractTermTuple) obj;
        return (this.term.equals(tmp.term) && this.curPos == tmp.curPos && this.freq == tmp.freq);
    }

    @Override
    public String toString() {
        return "term: " + this.term + ", freq: " + this.freq + ", curPos: " + this.curPos + "\n";
    }
}
