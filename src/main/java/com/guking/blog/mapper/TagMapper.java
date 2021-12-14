package com.guking.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guking.blog.pojo.Tag;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findTagById(Long id);

    List<Long> findHotTag(Integer limit);

    List<Tag> findHotTagById(List<Long> hotTag);
}
