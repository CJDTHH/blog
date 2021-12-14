package com.guking.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guking.blog.dos.ArticlesDos;
import com.guking.blog.pojo.Article;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select FROM_UNIXTIME(create_date/1000,'%Y') as year,FROM_UNIXTIME(create_date/1000,'%m') as month,count(*) as count from ms_article group by year,month")
    List<ArticlesDos> findListArchives();

    // Page<Article> page mybatis-Plus的分页配置对象
    IPage<Article> listArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month);
}
