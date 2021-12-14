package com.guking.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guking.blog.mapper.ArticleMapper;
import com.guking.blog.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {


    @Async("taskExecutor")
    public void updateArticelView(ArticleMapper mapper, Article article) {

        // 阅读数
        int viewCounts = article.getViewCounts();
        // 创建实例化对象,为了只修改你想修改的属性
        Article articleUpdate = new Article();
        // 修改所需要修改的属性
        articleUpdate.setViewCounts(viewCounts + 1);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getId,article.getId());
        wrapper.eq(Article::getViewCounts,viewCounts);
        mapper.update(articleUpdate,wrapper);
        try {
            Thread.sleep(5000);
            System.out.println("更新了....");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
