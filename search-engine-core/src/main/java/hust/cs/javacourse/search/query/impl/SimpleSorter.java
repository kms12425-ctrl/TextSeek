package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleSorter implements Sort {

    @Override
    public void sort(List<AbstractHit> hits) {
        Collections.sort(hits, new Comparator<AbstractHit>() {
            @Override
            public int compare(AbstractHit hit1, AbstractHit hit2) {
                int scoreComparison = Double.compare(hit2.getScore(), hit1.getScore());
                if (scoreComparison != 0) {
                    return scoreComparison;
                }
                return Integer.compare(hit1.getDocId(), hit2.getDocId());
            }
        });
    }

    @Override
    public double score(AbstractHit hit) {
        // 可以根据具体的算法来算分，例如这里简单用频次之和作为分值
        double score = 0;
        for (AbstractPosting posting : hit.getTermPostingMapping().values()) {
            score += posting.getFreq();
        }
        return score;
    }
}
