package com.wyy.myblog.controller.admin;

import com.wyy.myblog.service.BlogCategoryService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import com.wyy.myblog.util.Result;
import com.wyy.myblog.util.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/20 13:44 星期一
 * 分类管理
 */
@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Resource
    private BlogCategoryService mBlogCategoryService;

    /**
     * 获取分类页面
     * @param request
     * @return 分类页面
     */
    @GetMapping
    public String categoryWebPage(HttpServletRequest request) {
        request.setAttribute("path", "categories");
        return "admin/category";
    }


    /**
     * 分页获取分类数据
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
        PageResult data = mBlogCategoryService.getCategoriesPages(pageQuery);
        return ResultUtil.success(data);
    }

    /**
     * 添加分类
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<?> addCategory(@RequestParam("categoryName") String categoryName,
                                 @RequestParam("categoryIcon") String categoryIcon) {
        if (!StringUtils.hasText(categoryName) || !StringUtils.hasText(categoryIcon)) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogCategoryService.saveCategory(categoryName, categoryIcon)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("添加失败！");  // 可能是重复了，可能是数据库保存失败
    }

    /**
     * 修改分类
     * @param categoryId
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<?> editCategory(@RequestParam("categoryId") Integer categoryId,
                                  @RequestParam("categoryName") String categoryName,
                                  @RequestParam("categoryIcon") String categoryIcon) {
        if (categoryId == null || categoryId < 1 ||
                !StringUtils.hasText(categoryName) || !StringUtils.hasText(categoryIcon)) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogCategoryService.editCategory(categoryId, categoryName, categoryIcon)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("更新失败！");
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result<?> deleteCategories(@RequestBody Integer[] ids) {
        if (ids == null || ids.length < 1) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogCategoryService.batchDeleteCategories(ids)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("删除失败");
    }
}
