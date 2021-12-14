package com.guking.blog.controller;

import com.guking.blog.service.TagService;
import com.guking.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result hotTags() {
        int limit = 6;
        return tagService.hots(limit);
    }

    /**
     * 获取所有文章标签
     * @return
     */
    @GetMapping
    public Result findArticleLabel() {
        return tagService.findArticleLabel();
    }

    /**
     * 查询导航栏所有标签
     * @return
     */
    @GetMapping("/detail")
    public Result findTagsDetail() {
        return tagService.findTagsDeailt();
    }

    @GetMapping("/detail/{id}")
    public Result findTagsByID(@PathVariable("id") Long id) {
        return tagService.findTagsById(id);
    }
}
