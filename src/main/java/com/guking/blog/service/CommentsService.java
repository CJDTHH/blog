package com.guking.blog.service;

import com.guking.blog.vo.Result;
import com.guking.blog.vo.params.CommentsParam;

public interface CommentsService {

    /**
     * 评论列表
     * @param articleId
     * @return
     */
    public Result findCommentsById(Long articleId);

    /**
     * 新增评论
     * @param commentParam
     * @return
     */
    Result comment(CommentsParam commentParam);
}
