package com.guking.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guking.blog.dos.ArticlesDos;
import com.guking.blog.mapper.ArticleBodyMapper;
import com.guking.blog.mapper.ArticleMapper;
import com.guking.blog.mapper.ArticleTagMapper;
import com.guking.blog.pojo.Article;
import com.guking.blog.pojo.ArticleBody;
import com.guking.blog.pojo.ArticleTag;
import com.guking.blog.pojo.SysUser;
import com.guking.blog.service.*;
import com.guking.blog.util.UserThreadLocal;
import com.guking.blog.vo.ArticleBodyVo;
import com.guking.blog.vo.ArticleVo;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.TagVo;
import com.guking.blog.vo.params.ArticleParam;
import com.guking.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper mapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategorySerice categorySerice;
    @Autowired
    private ThreadService threadService;

    /**
     * 首页文章
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticle(PageParams pageParams) {
        // 分页查询数据库表
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.mapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }


    /*@Override
    public Result listArticle(PageParams params) {
        // 分页查询数据库表
        Page<Article> page = new Page<>(params.getPage(),params.getPageSize());
        // 设置查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (params.getCategoryId() != null) {
            wrapper.eq(Article::getCategoryId,params.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList<>();
        if (params.getTagId() != null){
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,params.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0){
                wrapper.in(Article::getId,articleIdList);
            }
        }
        // 是否置顶
        wrapper.orderByDesc(Article::getWeight);
        // 根据时间倒序排列
        wrapper.orderByDesc(Article::getCreateDate);
        Page<Article> selectPage = mapper.selectPage(page, wrapper);
        List<Article> records = selectPage.getRecords();
        // 将records list转移到voList中
        List<ArticleVo> voList = copyList(records,true,true);
        return Result.success(voList);
    }*/

    /**
     * 最热文章
     * @param limit 5
     * @return
     */
    @Override
    public Result findHotArticles(int limit) {
        // 设置查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 通过阅读量进行排序
        wrapper.orderByDesc(Article::getViewCounts);
        // 只取其的ID和title
        wrapper.select(Article::getId,Article::getTitle);
        // 只取5条
        wrapper.last("limit "+limit);
        List<Article> list = mapper.selectList(wrapper);
        return Result.success(copyList(list,false,false));
    }

    /**
     * 查询最新的文章
     * @param limit 5
     * @return
     */
    @Override
    public Result findNewArticles(int limit) {
        // 设置查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 查询最新的时间
        wrapper.orderByDesc(Article::getCreateDate);
        // 只查询 ID和Title
        wrapper.select(Article::getId,Article::getTitle);
        List<Article> list = mapper.selectList(wrapper);
        return Result.success(copyList(list, false, false));
    }

    /**
     * 文章归档
     * @return
     */
    @Override
    public Result findListArchives() {
        List<ArticlesDos> listArchives = mapper.findListArchives();
        return Result.success(listArchives);
    }

    /**
     * 根据id查看文章详情
     * @param id
     * @return
     */
    @Override
    public Result findViewList(Long id) {

        Article article = mapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true,true,true);
        threadService.updateArticelView(mapper,article);
        return Result.success(articleVo);
    }

    /**
     * 查找bodyId相关的数据
     * @param bodyId
     * @return
     */
    private ArticleBodyVo findArticlesBodyId(Long bodyId) {
        ArticleBody body = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(body.getContent());
        return articleBodyVo;
    }

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @Override
    public Result publish(ArticleParam articleParam) {
        // 获取用户信息
        SysUser sysUser = UserThreadLocal.get();
        // 实例化Article对象,将数据传入Article对象中
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        // 新增用户
        this.mapper.insert(article);
        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        mapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag, boolean isAuthor) {

        // 循环遍历,将数据进行转移,创建以ArticleVo为准的LIST集合,调用copy()方法进行数据转移;
        List<ArticleVo> list = new ArrayList<>();
        for (Article article : records) {
            list.add(copy(article,isTag,isAuthor,false,false));
        }
        return list;
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {

        // 循环遍历,将数据进行转移,创建以ArticleVo为准的LIST集合,调用copy()方法进行数据转移;
        List<ArticleVo> list = new ArrayList<>();
        for (Article article : records) {
            list.add(copy(article,isTag,isAuthor,isBody,isCategory));
        }
        return list;
    }

    private ArticleVo copy(Article article,boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {

        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        // 因为article中的creatData是long类型的,而articleVo是String类型的,所以需要转换 "yyyy-MM-dd HH:mm:ss"
        if (article.getCreateDate() !=null) {
            articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (isTag) {
            Long id = article.getId();
            articleVo.setTags(tagService.findListById(id));
        }

        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findByUserId(authorId).getNickname());
        }

        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categorySerice.findCateoryById(categoryId));
        }

        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticlesBodyId(bodyId));
        }
        return articleVo;
    }
}
