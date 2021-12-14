package com.guking.blog.service;

import com.guking.blog.pojo.SysUser;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.UserVo;

public interface SysUserService {

    SysUser findByUserId(Long id);

    /**
     *查询用户信息
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 根据token查找用户信息
     * @param token
     * @return
     */
    Result findUserByToekn(String token);

    /**
     * 判断账号是否存在
     * @param account
     * @return
     */
    SysUser findByAccount(String account);

    /**
     * 保存注册信息
     * @param sysUser
     * @return
     */
    int save(SysUser sysUser);

    UserVo findAuthorById(Long authorId);
}
