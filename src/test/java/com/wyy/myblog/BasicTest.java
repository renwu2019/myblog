package com.wyy.myblog;

import cn.hutool.core.date.DateUtil;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.wyy.myblog.entity.BlogLink;
import com.wyy.myblog.util.MyBlogUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created by 伍猷煜 on 2022/6/14 16:58 星期二
 */
public class BasicTest {


    @Test  // 类型转换测试
    public void testCast() {
        Integer a = 10;
        System.out.println(a.longValue());

        Object b = 10;
        Object c = "10";
        Object d = "a";
        // java.lang.Integer cannot be cast to java.lang.Long
        // Long b1 = (Long) b;  // fail
        System.out.println(new Long(a));  // success
        System.out.println(new Long(c.toString()));  // success
        System.out.println(new Long(d.toString()));  // fail
    }

    public String mdToHtmlForApiDoc(String md) {
        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                TocExtension.create()
        ));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(md);
        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
        // System.out.println(html);
        return html;
    }

    @Test
    public void testMarkDown2Html() {
        String md = "[TOC levels=1-]\n\n# 一级标题\n" +
                "\n" +
                "## 二级标题\n" +
                "\n" +
                "## 三级标题\n" +
                "\n" +
                "# 一级标题2\n" +
                "\n" +
                "## 二级标题2";
        String html = mdToHtmlForApiDoc(md);
        System.out.println(html);

    }

    @Test
    public void testURI() throws URISyntaxException {
        // URI uri = new URI("https" + "://" + "www.baidu.com" + ":" + 443);
        URI uri = new URI("/admin/blogs/upload/image/a.txt");
        System.out.println(uri);
        URI host = MyBlogUtils.getHost(uri);
        System.out.println(host);
    }

    @Test
    public void testInt() {
        System.out.println(Integer.MAX_VALUE);
        // 2147483647 长度为10
    }

    @Test
    public void testApi() {
        System.out.println(StringUtils.hasText("  "));  // false
        System.out.println(StringUtils.hasText(null));  // false
        System.out.println(StringUtils.hasText(""));  // false
    }

    @Test
    public void testStringObject(){
        Object a = new String("abc");
        Object b = a;
        System.out.println(b.getClass().getName());  // 输出java.lang.String
        System.out.println(b.toString());  // 输出 abc 而不是地址
    }

    // 泛型通配符测试，关键在于向上转型可以，向下转型不可以（可能有问题）
    // C -> B -> A
    class A{}
    class B extends A{}
    class C extends B{}
    public void printList0(List<?> list) {
        // list.add("c"); 不能添加
        // Object o = list.get(0); 可以读取，但需要人为理解向下强转才有意义，在编译器角度看是没有意义的，也就是不能读取。
    }
    // 只能传B及子类泛型参数 如B、C，List<B>或List<C>
    public void printList1(List<? extends B> list) {
        // 不能添加，编译报错。
        // 因为泛型不支持协变，添加B或及子类，且传入的是B的很小子孙类，那么这里添加就会出现问题（向下转型，编译器无法智能向下转型）
        // list.add(new B());
        B b = list.get(0);  // 可以读取，因为传入的都是B及子类，那么向上转型是可以的，因此可以读取，且用B接受
        System.out.println(list);
    }

    // 只能传B及父类泛型参数 如A、B，List<A>或List<B>
    public void printList2(List<? super B> list) {
        list.add(new B());  // 可以添加
        list.add(new C());  // 可以添加，向上转型为B
        // list.add(new A());  // 编译错误，因为B的父类是不确定的，因此不能传B的父类
        Object object = list.get(0);  // 可以读取，但是有可能不能向下转型，因此读取没有意义，可以理解为不能读取
        // 如果传入了A类型参数，则运行时错误 A cannot be cast to B
        // 如果传入了B类型参数，则可以强制转换
        // B b = (B)list.get(0);
        System.out.println(list);
    }
    @Test
    public void testGeneric() {
        A a = new A();
        B b = new B();
        C c = new C();
        List<A> listA = new ArrayList<>();listA.add(a);
        List<B> listB = new ArrayList<>();listB.add(b);
        List<C> listC = new ArrayList<>();listC.add(c);
        // 使用通配符的好处是，你可以传不确定的类型参数。
        // 方法1 只能传B及子类泛型参数 如B、C
        // printList1(listA);  // 编译报错
        printList1(listB);  // 类型参数是B
        printList1(listC);  // 类型参数是C

        // 方法2 只能传B及父类泛型参数 如A、B
        printList2(listA);  // 类型参数是A
        printList2(listB);  // 类型参数是B
        // printList2(listC);  // 编译报错
    }

    @Test
    public void testMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("k1", "v1");
        param.put("k2", 2);
        System.out.println(param.get("k3")); // 不会报错，如果没有key，就返回null
    }

    @Test
    public void testStream() {
        String[] tags = new String[]{"123", "abc", "a", "a"};
        List<String> collect = Arrays.stream(tags).distinct().collect(Collectors.toList());
        System.out.println(Arrays.toString(tags));
        System.out.println(collect);  // [123, abc, a]

        String[][] s = new String[][]{{"a", "b"}, {"c", "d"}, {"a", "e"}};
        // Map<String, String> collect1 = Arrays.stream(s).collect(Collectors.toMap(t -> t[0], t -> t[1]));
        // System.out.println(collect1); // java.lang.IllegalStateException: Duplicate key b
        // 第三个参数表示当有多个值对应一个键的时候，值应该选哪一个。
        // (key1, key2) -> key1)表示选前面出现的   (key1, key2) -> key2)表示选后面的
        Map<String, String> collect2 = Arrays.stream(s).collect(Collectors.toMap(t -> t[0], t -> t[1], (key1, key2) -> key2));
        System.out.println(collect2);  // {a=e, c=d}

        List<BlogLink> linkList = new ArrayList<>();
        Map<Byte, List<BlogLink>> collect1 = linkList.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
        System.out.println(collect1);
    }

    @Test
    public void testHuTool() {
        // DateUtil
        System.out.println(DateUtil.format(new Date(), "yyyyMMdd_HHmmss"));
    }
}
