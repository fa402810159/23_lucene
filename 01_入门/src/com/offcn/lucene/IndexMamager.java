package com.offcn.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * 索引库维护
 */
public class IndexMamager {
    IndexWriter indexWriter = null ;
    @Before
    public void before() throws Exception {
        //创建一个indexWriter对象，需要使用IKAnalyaer作为分析器
         indexWriter =
                new IndexWriter(FSDirectory.open(new File("D:\\myJava\\index").toPath()),
                        new IndexWriterConfig(new IKAnalyzer()));
    }


    //添加文档
    @Test
    public void addDocument() throws Exception{

        //创建一个Document对象
        Document document = new Document();
        //向document对象中添加域
        document.add(new TextField("name","新添加的文件", Field.Store.YES));
        document.add(new TextField("content","新添加的文件内容", Field.Store.YES));
        document.add(new TextField("path","D:\\a.txt", Field.Store.YES));
        //把文档写入索引库
        indexWriter.addDocument(document);
        //关闭索引库
        indexWriter.close();



    }

    //删除全部的索引库
    @Test
    public void deleteAllDocument() throws Exception{
        indexWriter.deleteAll();
        indexWriter.close();
    }

    //根据查询的结果删除
    @Test
    public void deleteDocumentByQuery() throws Exception{
        indexWriter.deleteDocuments(new Term("name","apache"));
        indexWriter.close();

    }

    //更新文档
    public void updateDocument() throws Exception{
        //创建一个文档对象
        Document document=new Document();
        //向文档对象中添加域
        document.add(new TextField("name","更新的文档", Field.Store.YES));
        document.add(new TextField("name1","更新的文档1", Field.Store.YES));
        //更新操作,查询出来名字带有spring的文档有两个，最终添加了1个，删除了两个。
        indexWriter.updateDocument(new Term("name","spring"),document);
        //关闭索引库
        indexWriter.close();


    }










}
