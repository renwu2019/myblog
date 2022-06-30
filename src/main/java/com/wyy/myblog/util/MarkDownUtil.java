package com.wyy.myblog.util;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Arrays;

public class MarkDownUtil {

    /**
     * 将入参Markdown文档转换为html文档
     * https://github.com/vsch/flexmark-java
     */
    public static String mdToHtml(String md) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                AutolinkExtension.create(),
                TocExtension.create()))
                .set(TocExtension.LEVELS, 255)
                .set(TocExtension.TITLE, "目录")
                .set(TocExtension.DIV_CLASS, "toc");
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse("[TOC]\n\n" + md);
        return renderer.render(document);
    }

}
