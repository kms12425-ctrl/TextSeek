package hust.cs.javacourse.search.index;

import java.util.List;

/**
 * <pre>
 * AbstractPostingList是所有PostingList对象的抽象父类.
 *      PostingList对象包含了一个单词的Posting列表.
 *      必须实现下面接口:
 *          FileSerializable：可序列化到文件或从文件反序列化.
 * </pre>
 */
public abstract class AbstractPostingList implements FileSerializable {
    /**
     * Posting列表，Posting必须是AbstractPosting子类型对象
     */
    protected List<AbstractPosting> list = new java.util.ArrayList<AbstractPosting>();

    /**
     * 添加Posting,要求不能有内容重复的posting
     * 
     * @param posting：Posting对象
     */
    public abstract void add(AbstractPosting posting);

    /**
     * 获得PosingList的字符串表示
     * 
     * @return ： PosingList的字符串表示
     */
    @Override
    public abstract String toString();

    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     * 
     * @param postings：Posting列表
     */
    public abstract void add(List<AbstractPosting> postings);

    /**
     * 返回指定下标位置的Posting
     * 
     * @param index ：下标
     *              @return： 指定下标位置的Posting
     */
    public abstract AbstractPosting get(int index);

    /**
     * 返回指定Posting对象的下标
     * 
     * @param posting：指定的Posting对象
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public abstract int indexOf(AbstractPosting posting);

    /**
     * 返回指定文档id的Posting对象的下标
     * 
     * @param docId ：文档id
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public abstract int indexOf(int docId);

    /**
     * 是否包含指定Posting对象
     * 
     * @param posting： 指定的Posting对象
     * @return : 如果包含返回true，否则返回false
     */
    public abstract boolean contains(AbstractPosting posting);

    /**
     * 删除指定下标的Posting对象
     * 
     * @param index：指定的下标
     */
    public abstract void remove(int index);

    /**
     * 删除指定的Posting对象
     * 
     * @param posting ：定的Posting对象
     */
    public abstract void remove(AbstractPosting posting);

    /**
     * 返回PosingList的元素个数
     * 
     * @return ：PosingList的元素个数
     */
    public abstract int size();

    /**
     * 清除PosingList
     */
    public abstract void clear();

    /**
     * 判断PosingList是否为空
     * 
     * @return 如果为空返回true;否则返回false
     */
    public abstract boolean isEmpty();

    /**
     * 对PosingList进行排序（按docId从小到大排序）同时对每个Posting里的positions从小到大排序
     */
    public abstract void sort();
}
