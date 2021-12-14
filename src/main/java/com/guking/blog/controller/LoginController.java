package com.guking.blog.controller;

import com.guking.blog.service.LoginService;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录功能x
     * @param loginParam
     * @return
     */
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam) {
        // 登录访问用户表
        return loginService.login(loginParam);
    }
}

