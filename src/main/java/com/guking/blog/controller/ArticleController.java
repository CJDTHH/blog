package com.guking.blog.controller;

import com.guking.blog.common.aop.LogAnnotation;
import com.guking.blog.service.ArticleService;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.params.ArticleParam;
import com.guking.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// json数据交换
@RestController
@RequestMapping("articles")
public class ArticleController {
    Integer limit = 5;

    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     * @param params
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams params) {
        return articleService.listArticle(params);
    }

    /**
     * 最热文章,前5条
     * @return
     */
    @PostMapping("/hot")
    public Result hotArticles() {
        return articleService.findHotArticles(limit);
    }

    /**
     * 最新文章,前5条
     * @return
     */
    @PostMapping("/new")
    public Result newArticles() {
        return articleService.findNewArticles(limit);
    }

    /**
     * 文章归档,显示某年某月多少篇文章
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives() {
        return articleService.findListArchives();
    }


    /**
     * 文章详情
     * @param id
     * @return
     */
    @PostMapping("/view/{id}")
    public Result viewList(@PathVariable("id") Long id) {
        return articleService.findViewList(id);
    }


    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @PostMapping("/publish")
    // 加上此接口,代表要对此接口记录日志
    @LogAnnotation(module = "文章",operation = "获取文章列表")
    public Result publishController(@RequestBody ArticleParam articleParam) {

        return articleService.publish(articleParam);
    }


}
