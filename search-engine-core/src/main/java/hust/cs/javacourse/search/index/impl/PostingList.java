package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * AbstractPostingList的具体实现类
 */
public class PostingList extends AbstractPostingList {
    @Override
    public void add(AbstractPosting posting) {
        if (this.indexOf(posting.getDocId()) == -1)
            this.list.add(posting);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (AbstractPosting tmp : this.list) {
            sb.append(tmp.toString());
        }
        return sb.toString();
    }

    @Override
    public void add(List<AbstractPosting> postings) {
        for (AbstractPosting tmp : postings) {
            this.add(tmp);
        }
    }

    @Override
    public AbstractPosting get(int index) {
        return this.list.get(index);
    }

    @Override
    public int indexOf(AbstractPosting posting) {
        if (posting == null) {
            return -1;
        }
        return this.indexOf(posting.getDocId());
    }

    @Override
    public int indexOf(int docId) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getDocId() == docId)
                return i;
        }
        return -1;
    }

    @Override
    public boolean contains(AbstractPosting posting) {
        return this.indexOf(posting) != -1;
    }

    @Override
    public void remove(int index) {
        this.list.remove(index);
    }

    @Override
    public void remove(AbstractPosting posting) {
        int index = this.indexOf(posting);
        if (index != -1) {
            this.list.remove(index);
        }
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public void sort() {
        Collections.sort(this.list);
        for (AbstractPosting tmp : this.list) {
            tmp.sort();
        }
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(this.list.size());
            for (AbstractPosting posting : this.list) {
                posting.writeObject(out);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readObject(ObjectInputStream in) {
        try {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                Posting posting = new Posting();
                posting.readObject(in);
                this.list.add(posting);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
