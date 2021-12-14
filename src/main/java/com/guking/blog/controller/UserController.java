package com.guking.blog.controller;

import com.guking.blog.service.SysUserService;
import com.guking.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户信息,根据token来查询
     * @param token
     * @return
     */
    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {

        return sysUserService.findUserByToekn(token);
    }
}
