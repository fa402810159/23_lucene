package com.offcn.lucene;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.sound.midi.Soundbank;
import java.io.File;

/**
 * 创建索引库
 */
public class LuceneFirst {
    @Test
    public void createIndex() throws Exception {
        //创建一个Director对象,指定索引库保存的位置
        //把索引库保存在磁盘
        Directory directory= FSDirectory.open(new File("D:\\myJava\\index").toPath());
        //2、基于Directory对象创建一个IndexWriter对象 使用的是中文分析器
        IndexWriterConfig config =new IndexWriterConfig(new IKAnalyzer());
        IndexWriter indexWriter=new IndexWriter(directory,config);
        //3、读取磁盘上的文件，对应每个文件创建一个文档对象。
        File dir =new File("E:\\BaiduNetdiskDownload\\阶段4 流行框架\\资料\\01.Lucene\\lucene\\02.参考资料\\searchsource");
        File[] files = dir.listFiles();
        for (File f : files) {
            //取文件名
            String fName = f.getName();
            //文件的路径
            String fPath = f.getPath();
            //文件的内容
            String fileContent = FileUtils.readFileToString(f, "utf-8");
            //文件的大小
            long fSize = FileUtils.sizeOf(f);
            //创建域 field
            //参数1：域的名称，参数2：域的内容，参数3：是否存储
            Field fieldName =new TextField("name",fName, Field.Store.YES);
            Field fieldPath =new TextField("path",fPath, Field.Store.YES);
            Field fieldContent =new TextField("content",fileContent, Field.Store.YES);
            Field fieldSize =new TextField("size",fSize+"", Field.Store.YES);
            //创建文档对象
            Document document=new Document();
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);
            //把文档对象写入索引库
            indexWriter.addDocument(document);

        }
        //关闭indexWriter对象
        indexWriter.close();


    }


    //查询索引库
    @Test
    public void searchIndex() throws Exception {
        //创建一个Director对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("D:\\myJava\\index").toPath());
        //创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建一个IndexSearcher对象，构造方法中的参数indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //创建一个query对象，TermQuery 根据关键词查找
        Query query = new TermQuery(new Term("content", "spring"));
        //执行查询，得到一个TopDocs对象
        //参数1，查询对象  参数2 查询结果返回的最大记录数
        TopDocs topDocs = indexSearcher.search(query, 10);
        //取查询结果的总记录数
        System.out.println("查询总记录数" + topDocs.totalHits);
        //取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc : scoreDocs) {
            //取文档id
            int docId = doc.doc;
            //根据id取文档对象
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println("-----------------寂寞的分割线");
        }
        //关闭indexReader对象
        indexReader.close();

    }


    //分析器的分析效果
    @Test
    public void testTokenStream() throws Exception{
        //创建一个Analyzer对象 standardAnalyzer对象
        Analyzer analyzer=new StandardAnalyzer();
       //中文分析器 analyzer analyzer1=new IKAnalyzer();
        //使用分析器对象的tokenStream方法获得一个TokenStream对象
        TokenStream tokenStream= analyzer.tokenStream("","2017年12月14日 -Lucence");
        //向tokenstram对象中设置一个引用，相当于一个指针
        CharTermAttribute charTermAttribute=tokenStream.addAttribute(CharTermAttribute.class);
        //调用tokenstream对象的rest方法，如果不调用抛异常
        tokenStream.reset();
        //使用while循环遍历tokenStream对象
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        //关闭
        tokenStream.close();

    }



}
