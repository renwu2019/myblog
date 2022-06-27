package com.wyy.myblog.controller.admin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.wyy.myblog.config.Constants;
import com.wyy.myblog.entity.Blog;
import com.wyy.myblog.service.BlogCategoryService;
import com.wyy.myblog.service.BlogService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import com.wyy.myblog.util.Result;
import com.wyy.myblog.util.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/21 14:54 星期二
 * 博客管理
 */
@Controller
@RequestMapping("/admin/blogs")
public class BlogController {

    @Resource
    private BlogService mBlogService;

    @Resource
    private BlogCategoryService mBlogCategoryService;

    @GetMapping()
    public String blogWebPage(HttpServletRequest request) {
        request.setAttribute("path", "blogs");
        return "admin/blog";
    }

    /**
     * 分页获取博客数据
     *
     * @param params
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<?> list(@RequestParam Map<String, Object> params) {
        if (!params.containsKey("page") || !params.containsKey("limit")) {
            return ResultUtil.fail(404, "参数异常！");
        }
        PageQuery pageQuery = new PageQuery(params);
        PageResult data = mBlogService.getBlogsPages(pageQuery);
        return ResultUtil.success(data);
    }

    /**
     * 获取博客编辑页面
     *
     * @return
     */
    @GetMapping("/edit")
    public String blogEditWebPage(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("categories", mBlogCategoryService.getAllCategories());
        return "admin/edit";
    }

    /**
     * 获取指定博客编辑页面
     *
     * @param request
     * @param blogId
     * @return
     */
    @GetMapping("/edit/{blogId}")
    public String blogEditWebPage(HttpServletRequest request, @PathVariable("blogId") Long blogId) {
        request.setAttribute("path", "edit");
        Blog blog = mBlogService.getBlogById(blogId);
        if (blog == null) {
            return "error/error_404";
        }
        request.setAttribute("blog", blog);
        request.setAttribute("categories", mBlogCategoryService.getAllCategories());
        return "admin/edit";
    }

    /**
     * 新增博客
     * @param blogTitle
     * @param blogSubUrl
     * @param blogCategoryId
     * @param blogTags
     * @param blogContent
     * @param blogCoverImage
     * @param blogStatus
     * @param enableComment
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(@RequestParam("blogTitle") String blogTitle,
                          @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                          @RequestParam("blogCategoryId") Integer blogCategoryId,
                          @RequestParam("blogTags") String blogTags,
                          @RequestParam("blogContent") String blogContent,
                          @RequestParam("blogCoverImage") String blogCoverImage,
                          @RequestParam("blogStatus") Byte blogStatus,
                          @RequestParam("enableComment") Byte enableComment) {
        if (!StringUtils.hasText(blogTitle)) {
            return ResultUtil.fail("请输入文章标题");
        }
        if (blogTitle.length() > 150) {
            return ResultUtil.fail("文章标题过长");
        }
        if (blogSubUrl != null && blogSubUrl.trim().length() > 150) {
            return ResultUtil.fail("路径过长");
        }
        if (!StringUtils.hasText(blogTags)) {
            return ResultUtil.fail("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultUtil.fail("标签过长");
        }
        if (!StringUtils.hasText(blogContent)) {
            return ResultUtil.fail("请输入文章内容");
        }
        if (blogContent.length() > 100000) {
            return ResultUtil.fail("文章内容过长");
        }
        if (!StringUtils.hasText(blogCoverImage)) {
            return ResultUtil.fail("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String result = mBlogService.saveBlog(blog);
        if ("success".equals(result)) {
            return ResultUtil.success("添加成功", null);
        } else return ResultUtil.fail(result);
    }

    /**
     * 更新博客
     * @param blogId
     * @param blogTitle
     * @param blogSubUrl
     * @param blogCategoryId
     * @param blogTags
     * @param blogContent
     * @param blogCoverImage
     * @param blogStatus
     * @param enableComment
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<?> update(@RequestParam("blogId") Long blogId,
                            @RequestParam("blogTitle") String blogTitle,
                            @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                            @RequestParam("blogCategoryId") Integer blogCategoryId,
                            @RequestParam("blogTags") String blogTags,
                            @RequestParam("blogContent") String blogContent,
                            @RequestParam("blogCoverImage") String blogCoverImage,
                            @RequestParam("blogStatus") Byte blogStatus,
                            @RequestParam("enableComment") Byte enableComment) {
        if (!StringUtils.hasText(blogTitle)) {
            return ResultUtil.fail("请输入文章标题");
        }
        if (blogTitle.length() > 150) {
            return ResultUtil.fail("文章标题过长");
        }
        if (blogSubUrl != null && blogSubUrl.trim().length() > 150) {
            return ResultUtil.fail("路径过长");
        }
        if (!StringUtils.hasText(blogTags)) {
            return ResultUtil.fail("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultUtil.fail("标签过长");
        }
        if (!StringUtils.hasText(blogContent)) {
            return ResultUtil.fail("请输入文章内容");
        }
        if (blogContent.length() > 100000) {
            return ResultUtil.fail("文章内容过长");
        }
        if (!StringUtils.hasText(blogCoverImage)) {
            return ResultUtil.fail("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String result = mBlogService.updateBlogById(blog);
        if ("success".equals(result)) {
            return ResultUtil.success("添加成功", null);
        } else return ResultUtil.fail(result);
    }

    /**
     * 删除博客
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result<?> delete(@RequestBody Integer[] ids) {
        if (ids == null || ids.length < 1) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogService.batchDeleteBlogs(ids)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("删除失败");
    }

    /**
     * 图片文件上传 博客封面 | Markdown编辑器粘贴上传
     */
    @PostMapping("/upload/image")
    @ResponseBody
    public Result<?> uploadImage(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResultUtil.fail("参数异常");
        }
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = DateUtil.format(new Date(), "yyyyMMdd_HHmmss_") +
                RandomUtil.randomNumbers(3) + suffixName;
        File dir = new File(Constants.FILE_UPLOAD_DIC);
        File destFile = new File(dir, newFileName);
        try {
            // 创建文件夹
            if (!dir.exists()) {
                // dir.setWritable(true, false);    //设置写权限，windows下不用此语句
                if (!dir.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + dir);
                }
            }
            // 保存图片
            file.transferTo(destFile);
            // 返回图片访问路径
            // String fileUrl = MyBlogUtils.getHost(new URI(request.getRequestURL() + ""))
            //         + Constants.RESOURCE_PATH_PREFIX + newFileName;
            String fileUrl = Constants.RESOURCE_PATH_PREFIX + newFileName;
            return ResultUtil.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.fail("上传失败");
        }
    }


    /**
     * Markdown文件上传 resources\static\admin\plugins\editormd\examples\php
     * editormd的使用
     * https://github.com/pandao/editor.md
     * https://pandao.github.io/editor.md/
     * https://www.jianshu.com/p/bc278d552567
     */
    @PostMapping("/md/uploadfile")
    public void uploadFileByEditormd(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam("editormd-image-file") MultipartFile file) throws IOException{
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/html");
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            response.getWriter().write("{\"success\":0}");
            return;
        }
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = DateUtil.format(new Date(), "yyyyMMdd_HHmmss_") +
                RandomUtil.randomNumbers(3) + suffixName;
        File dir = new File(Constants.FILE_UPLOAD_DIC);
        File destFile = new File(dir, newFileName);

        try {
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + dir);
                }
            }
            file.transferTo(destFile);
            // 返回图片访问路径
            // String fileUrl = MyBlogUtils.getHost(new URI(request.getRequestURL() + ""))
            //         + Constants.RESOURCE_PATH_PREFIX + newFileName;
            String fileUrl = Constants.RESOURCE_PATH_PREFIX + newFileName;
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + fileUrl + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":0}");
        }
    }
}
