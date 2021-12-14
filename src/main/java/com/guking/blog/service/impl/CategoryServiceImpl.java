package com.guking.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guking.blog.mapper.CategoryMapper;
import com.guking.blog.pojo.Category;
import com.guking.blog.service.CategorySerice;
import com.guking.blog.vo.CategoryVo;
import com.guking.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service
public class CategoryServiceImpl implements CategorySerice {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询类别
     * @param categoryId
     * @return
     */
    @Override
    public CategoryVo findCateoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    /**
     * 查询所有文章的分类
     * @return
     */
    @Override
    public Result findCateoryClassificatoin() {

        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());

        return Result.success(copyList(categories));
    }

    @Override
    public Result findNavigationCategortys() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(null);
        return Result.success(copyList(categories));
    }

    @Override
    public Result findNavigationById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }


    // 将List<Category>转入List<CategoryVo>中
    private List<CategoryVo> copyList(List<Category> categories) {
        List<CategoryVo> list = new ArrayList<>();
        for (Category category : categories) {
            list.add(copy(category));
        }
        return list;
    }


    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

}
