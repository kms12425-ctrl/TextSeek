package hust.cs.javacourse.search.index.impl;

import java.io.File;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.util.FileUtil;

/**
 * AbstractIndexBuilder的具体实现类
 */
public class IndexBuilder extends AbstractIndexBuilder {
    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }

    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        Index index = new Index();

        for (String filepath : FileUtil.list(rootDirectory, ".txt")) {
            File file = new File(filepath);
            AbstractDocument document = this.docBuilder.build(docId, file.getPath(), file);
            index.addDocument(document);
            this.docId++;
        }
        index.optimize();
        // 注意：save() 由调用方决定何时执行和保存到哪个路径
        return index;
    }
}
