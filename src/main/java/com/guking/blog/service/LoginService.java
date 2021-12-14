package com.guking.blog.service;

import com.guking.blog.pojo.SysUser;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface LoginService {


    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 解析token是否为空,是否合法,redis是否存在
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 退出
     * @param token
     * @return
     */
    Result loginOut(String token);

    /**
     * 注册
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
