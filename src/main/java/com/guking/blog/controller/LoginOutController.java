package com.guking.blog.controller;

import com.guking.blog.service.LoginService;
import com.guking.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LoginOutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result loginOut(@RequestHeader("Authorization") String token) {
        return loginService.loginOut(token);
    }

}
