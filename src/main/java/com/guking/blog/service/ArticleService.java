package com.guking.blog.service;

import com.guking.blog.vo.Result;
import com.guking.blog.vo.params.ArticleParam;
import com.guking.blog.vo.params.PageParams;

public interface ArticleService {

    /**
     * 首页文章列表
     * @param params
     * @return
     */
    Result listArticle(PageParams params);

    /**
     * 最热文章
     * @param limit 5
     * @return
     */
    Result findHotArticles(int limit);

    /**
     * 最新文章
     * @param limit 5
     * @return
     */
    Result findNewArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result findListArchives();


    /**
     * 文章详情
     * @param id
     * @return
     */
    Result findViewList(Long id);

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}
