package hust.cs.javacourse.search.query.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

/**
 * AbstractIndexSearcher的具体实现类
 */
public class IndexSearcher extends AbstractIndexSearcher {
    @Override
    public void open(String indexFile) {
        this.index.load(new File(indexFile));
    }

    /**
     * 直接设置内存中的索引对象（跳过文件加载）
     */
    public void setIndex(AbstractIndex index) {
        this.index = index;
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postingList = this.index.search(queryTerm);
        if (postingList == null || postingList.isEmpty()) {
            return new AbstractHit[0];
        }

        List<AbstractHit> hits = new ArrayList<AbstractHit>();

        for (int i = 0; i < postingList.size(); i++) {
            AbstractPosting posting = postingList.get(i);

            Map<AbstractTerm, AbstractPosting> map = new TreeMap<AbstractTerm, AbstractPosting>();
            map.put(queryTerm, posting);

            Hit hit = new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), map);
            hit.setScore(sorter.score(hit));
            hits.add(hit);
        }

        sorter.sort(hits);
        return hits.toArray(new AbstractHit[0]);
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter,
            LogicalCombination combine) {

        AbstractPostingList list1 = this.index.search(queryTerm1);
        AbstractPostingList list2 = this.index.search(queryTerm2);

        int size1 = (list1 == null) ? 0 : list1.size();
        int size2 = (list2 == null) ? 0 : list2.size();

        if (combine == LogicalCombination.AND) {
            if (size1 == 0 || size2 == 0) {
                return new AbstractHit[0];
            }

            List<AbstractHit> hits = new ArrayList<AbstractHit>();
            int i = 0;
            int j = 0;

            while (i < size1 && j < size2) {
                AbstractPosting p1 = list1.get(i);
                AbstractPosting p2 = list2.get(j);

                if (p1.getDocId() == p2.getDocId()) {
                    Map<AbstractTerm, AbstractPosting> map = new TreeMap<AbstractTerm, AbstractPosting>();
                    map.put(queryTerm1, p1);
                    map.put(queryTerm2, p2);

                    Hit hit = new Hit(p1.getDocId(), this.index.getDocName(p1.getDocId()), map);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);

                    i++;
                    j++;
                } else if (p1.getDocId() < p2.getDocId()) {
                    i++;
                } else {
                    j++;
                }
            }

            sorter.sort(hits);
            return hits.toArray(new AbstractHit[0]);

        } else { // LogicalCombination.OR
            List<AbstractHit> hits = new ArrayList<AbstractHit>();
            int i = 0;
            int j = 0;

            while (i < size1 && j < size2) {
                AbstractPosting p1 = list1.get(i);
                AbstractPosting p2 = list2.get(j);

                if (p1.getDocId() == p2.getDocId()) {
                    Map<AbstractTerm, AbstractPosting> map = new TreeMap<AbstractTerm, AbstractPosting>();
                    map.put(queryTerm1, p1);
                    map.put(queryTerm2, p2);
                    Hit hit = new Hit(p1.getDocId(), this.index.getDocName(p1.getDocId()), map);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);
                    i++;
                    j++;
                } else if (p1.getDocId() < p2.getDocId()) {
                    Map<AbstractTerm, AbstractPosting> map = new TreeMap<AbstractTerm, AbstractPosting>();
                    map.put(queryTerm1, p1);
                    Hit hit = new Hit(p1.getDocId(), this.index.getDocName(p1.getDocId()), map);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);
                    i++;
                } else {
                    Map<AbstractTerm, AbstractPosting> map = new TreeMap<AbstractTerm, AbstractPosting>();
                    map.put(queryTerm2, p2);
                    Hit hit = new Hit(p2.getDocId(), this.index.getDocName(p2.getDocId()), map);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);
                    j++;
                }
            }

            while (i < size1) {
                AbstractPosting p1 = list1.get(i);
                Map<AbstractTerm, AbstractPosting> map = new TreeMap<AbstractTerm, AbstractPosting>();
                map.put(queryTerm1, p1);
                Hit hit = new Hit(p1.getDocId(), this.index.getDocName(p1.getDocId()), map);
                hit.setScore(sorter.score(hit));
                hits.add(hit);
                i++;
            }

            while (j < size2) {
                AbstractPosting p2 = list2.get(j);
                Map<AbstractTerm, AbstractPosting> map = new TreeMap<AbstractTerm, AbstractPosting>();
                map.put(queryTerm2, p2);
                Hit hit = new Hit(p2.getDocId(), this.index.getDocName(p2.getDocId()), map);
                hit.setScore(sorter.score(hit));
                hits.add(hit);
                j++;
            }

            sorter.sort(hits);
            return hits.toArray(new AbstractHit[0]);
        }
    }
}
