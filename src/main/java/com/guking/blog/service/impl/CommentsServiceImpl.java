package com.guking.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guking.blog.mapper.CommentsMapper;
import com.guking.blog.pojo.Comment;
import com.guking.blog.pojo.SysUser;
import com.guking.blog.service.CommentsService;
import com.guking.blog.service.SysUserService;
import com.guking.blog.util.UserThreadLocal;
import com.guking.blog.vo.CommentsVo;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.UserVo;
import com.guking.blog.vo.params.CommentsParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private SysUserService sysUserService;



    /**
     * 查询评论列表
     * @param articleId
     * @return
     */
    @Override
    public Result findCommentsById(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId,articleId);
        wrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentsMapper.selectList(wrapper);
        List<CommentsVo> commentsVo = copyList(comments);
        return Result.success(commentsVo);
    }

    /**
     * 新增评论
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentsParam commentParam) {
        // 获取当前用户
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentsMapper.insert(comment);
        return Result.success(comment);
    }


    private List<CommentsVo> copyList(List<Comment> comments) {
        List<CommentsVo> list = new ArrayList<>();
        for (Comment comment : comments) {
            list.add(copy(comment));
        }
        return list;
    }

    private CommentsVo copy(Comment comment) {
        CommentsVo commentsVo = new CommentsVo();
        BeanUtils.copyProperties(comment, commentsVo);
        // 作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findAuthorById(authorId);
        commentsVo.setAuthor(userVo);
        // 子评论
        Integer level = comment.getLevel();
        if (1 == level) {
            Long id = comment.getId();
            List<CommentsVo> commentLevel = findLevelById(id);
            commentsVo.setChildrens(commentLevel);
        }

        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo authorById = this.sysUserService.findAuthorById(toUid);
            commentsVo.setToUser(authorById);
        }
        return commentsVo;
    }

    private List<CommentsVo> findLevelById(Long id) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId,id);
        wrapper.eq(Comment::getLevel,2);
        return copyList(commentsMapper.selectList(wrapper));
    }
}
