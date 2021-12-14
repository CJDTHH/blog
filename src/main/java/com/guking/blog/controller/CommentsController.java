package com.guking.blog.controller;

import com.guking.blog.service.CommentsService;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.params.CommentsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    /**
     * 查询参数列表
     * @param articleId
     * @return
     */
    @GetMapping("/article/{id}")
    public Result commentList(@PathVariable("id") Long articleId) {
        return commentsService.findCommentsById(articleId);
    }


    /**
     * 评论
     * @param commentParam
     * @return
     */
    @PostMapping("/create/change")
    public Result change(@RequestBody CommentsParam commentParam) {
        return commentsService.comment(commentParam);
    }
}
