package com.guking.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guking.blog.mapper.TagMapper;
import com.guking.blog.pojo.Category;
import com.guking.blog.pojo.Tag;
import com.guking.blog.service.TagService;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findListById(Long id) {
        //  mybatis-plus无法多表关联
        List<Tag> tags = tagMapper.findTagById(id);
        return copyList(tags);
    }


    @Override
    public Result hots(int limit) {
        List<Long> hotTag = tagMapper.findHotTag(limit);
        // 如果list为空，给个空值
        if (hotTag.isEmpty()) {
            return Result.success(Collections.emptyList());
        }
        List<Tag> list = tagMapper.findHotTagById(hotTag);
        return Result.success(list);
    }

    /**
     * 获取所有文章标签
     * @return
     */
    @Override
    public Result findArticleLabel() {
        List<Tag> tags = tagMapper.selectList(null);
        return Result.success(copyList(tags));
    }

    /**
     *  查询导航栏所有标签
     * @return
     */
    @Override
    public Result findTagsDeailt() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = tagMapper.selectList(null);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findTagsById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVo copy = copy(tag);
        return Result.success(copy);
    }


    private List<TagVo> copyList(List<Tag> tags) {

        List<TagVo> list = new ArrayList<>();
        for (Tag tag : tags) {
            list.add(copy(tag));
        }
        return list;
    }

    public TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
