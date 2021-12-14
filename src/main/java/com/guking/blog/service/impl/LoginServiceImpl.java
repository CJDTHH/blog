package com.guking.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.guking.blog.pojo.SysUser;
import com.guking.blog.service.LoginService;
import com.guking.blog.service.SysUserService;
import com.guking.blog.util.JWTUtils;
import com.guking.blog.vo.ErrorCode;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String slat = "mszlu!@#";


    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    @Override
    public Result login(LoginParam loginParam) {
        // 1.检查参数是否合法
        // 2.根据用户名在数据库中查找,存在返回JWT生成token,反对给前端
        // 3.将token放入redis中,redis token:user设置过期时间(登录认证,先认证token字符串是否合法,然后再去redis中认证是否存在)
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        // 如果account 和 password为空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password+slat);
        SysUser user = userService.findUser(account, password);
        if (user == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(user.getId());
        // 再将token放入redis中.设置过期时间 1天
        redisTemplate.opsForValue().set("TOKEN"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        // 返回给前端
        return Result.success(token);
    }

    /**
     * 解析token是否为空,是否合法,redis是否存在
     * @param token
     * @return
     */
    @Override
    public SysUser checkToken(String token) {
        // 是否为空
        if (StringUtils.isBlank(token)) {
            return null;
        }
        // 是否合法
        if (JWTUtils.checkToken(token) == null) {
            return null;
        }
        // redis是否存在
        String userToken = redisTemplate.opsForValue().get("TOKEN" + token);
        if (StringUtils.isBlank(userToken)) {
            return null;
        }
        // 将userTOKEN解析成一个SysUser对象
        SysUser sysUser = JSON.parseObject(userToken, SysUser.class);
        return sysUser;
    }

    /**
     * 直接在redis中删除token
     * @param token
     * @return
     */
    @Override
    public Result loginOut(String token) {
        redisTemplate.delete("TOKEN"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        /**
         *  1. 判断参数是否合法
         *  2. 判断账户是否存在
         *  3. 生成toekn
         *  4. 存入redis,并返回
         *  5. 注意要加上事务,一旦中间有什么过程发生错误,注册的用户需要回滚
         */
        String nickname = loginParam.getNickname();
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        // 1. 判断参数是否合法
        if (StringUtils.isBlank(nickname) || StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 2. 判断账户是否存在
        SysUser sysUser = userService.findByAccount(account);
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        //1 为true
        sysUser.setAdmin(1);
        // 0 为false
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.userService.save(sysUser);
        // 生成token
        String token = JWTUtils.createToken(sysUser.getId());
        // 存入redis
        redisTemplate.opsForValue().set("TOKEN"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }


}
