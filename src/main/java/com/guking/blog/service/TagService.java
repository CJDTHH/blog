package com.guking.blog.service;

import com.guking.blog.pojo.Article;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findListById(Long id);

    Result hots(int limit);

    /**
     * 获取所有文章标签
     * @return
     */
    Result findArticleLabel();

    /**
     *  查询导航栏所有标签
     * @return
     */
    Result findTagsDeailt();

    Result findTagsById(Long id);
}
