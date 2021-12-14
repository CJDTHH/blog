package com.guking.blog.service;

import com.guking.blog.vo.CategoryVo;
import com.guking.blog.vo.Result;

import java.util.List;

public interface CategorySerice {

    /**
     * 查询类别
     * @param categoryId
     * @return
     */
    CategoryVo findCateoryById(Long categoryId);

    /**
     * 查询所有文章的分类
     * @return
     */
    Result findCateoryClassificatoin();

    /**
     * 查询导航栏所有文章的分类
     * @return
     */
    Result findNavigationCategortys();


    Result findNavigationById(Long id);
}
