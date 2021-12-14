package com.guking.blog.vo;

import lombok.Data;

@Data
public class LoginUserVo {

    private Long id;
    // 账号
    private String account;
    // 密码
    private String nickname;

    private String avatar;
}
