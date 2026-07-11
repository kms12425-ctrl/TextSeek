package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AbstractPosting的具体实现类
 */
public class Posting extends AbstractPosting {
    public Posting() {
        super();
    }

    public Posting(int docId, int freq, List<Integer> positions) {
        super(docId, freq, positions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AbstractPosting))
            return false;
        AbstractPosting tmp = (AbstractPosting) obj;
        if (tmp.getDocId() != this.docId || tmp.getFreq() != this.freq) {
            return false;
        }

        if (tmp.getPositions() == null || this.positions == null) {
            return tmp.getPositions() == this.positions;
        }

        if (tmp.getPositions().size() != this.positions.size()) {
            return false;
        }

        List<Integer> otherPositions = new ArrayList<Integer>(tmp.getPositions());
        List<Integer> currentPositions = new ArrayList<Integer>(this.positions);
        Collections.sort(otherPositions);
        Collections.sort(currentPositions);
        return currentPositions.equals(otherPositions);
    }

    @Override
    public String toString() {
        return "docID: " + this.docId + ", freq: " + this.freq + ", positions: " + this.positions + "\n";
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
    public int getFreq() {
        return this.freq;
    }

    @Override
    public void setFreq(int freq) {
        this.freq = freq;
    }

    @Override
    public List<Integer> getPositions() {
        return this.positions;
    }

    @Override
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    @Override
    public int compareTo(AbstractPosting o) {
        return this.docId - o.getDocId();
    }

    @Override
    public void sort() {
        Collections.sort(this.positions);
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.docId);
            out.writeObject(this.freq);
            out.writeObject(this.positions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docId = (Integer) (in.readObject());
            this.freq = (Integer) (in.readObject());
            this.positions = (List<Integer>) (in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
