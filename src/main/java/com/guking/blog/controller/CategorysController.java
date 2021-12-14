package com.guking.blog.controller;

import com.guking.blog.service.CategorySerice;
import com.guking.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categorys")
public class CategorysController {

    @Autowired
    private CategorySerice categorySerice;

    /**
     * 查询所有文章的分类
     * @return
     */
    @GetMapping
    public Result categortysClassification() {
        return categorySerice.findCateoryClassificatoin();
    }

    /**
     * 查询导航栏所有文章的分类
     * @return
     */
    @GetMapping("/detail")
    public Result navigationCategortys() {
        return categorySerice.findNavigationCategortys();
    }

    @GetMapping("/detail/{id}")
    public Result findNavigationById(@PathVariable("id") Long id) {
        return categorySerice.findNavigationById(id);
    }


}
