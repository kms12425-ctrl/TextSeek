package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

/**
 * AbstractTerm的具体实现类
 */
public class Term extends AbstractTerm {
    public Term() {
        super();
    }

    public Term(String content) {
        super(content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AbstractTerm))
            return false;
        AbstractTerm tmp = (AbstractTerm) obj;
        return (Objects.equals(tmp.getContent(), this.content));
    }

    @Override
    public String toString() {
        return this.content;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(AbstractTerm o) {
        return this.content.compareTo(o.getContent());
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.content = (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
