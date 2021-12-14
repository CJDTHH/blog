package com.guking.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommentsVo {

    private Long id;

    private UserVo author;

    private String content;

    private List<CommentsVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
