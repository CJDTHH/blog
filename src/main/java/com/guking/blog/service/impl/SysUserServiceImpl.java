package com.guking.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guking.blog.mapper.SysUserMapper;
import com.guking.blog.pojo.SysUser;
import com.guking.blog.service.LoginService;
import com.guking.blog.service.SysUserService;
import com.guking.blog.vo.ErrorCode;
import com.guking.blog.vo.LoginUserVo;
import com.guking.blog.vo.Result;
import com.guking.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;


    @Override
    public SysUser findByUserId(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("GuKing");
        }
        return sysUser;
    }

    /**
     * 查询用户信息
     * @param account
     * @param password
     * @return
     */
    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount,account);
        wrapper.eq(SysUser::getPassword,password);
        wrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        wrapper.last("limit "+1);
        return sysUserMapper.selectOne(wrapper);
    }

    /**
     * 根据toekn来查找用户信息
     * @param token
     * @return
     */
    @Override
    public Result findUserByToekn(String token) {
        /**
         * 1.token合法性校验: [是否为空,解析是否成功,redis是否存在]
         * 2.失败:返回错误
         * 3.成功:返回对应的结果
         */

        // 解析是否成功
        SysUser user = loginService.checkToken(token);
        if (user == null) {
            Result.fail(ErrorCode.TOKEN_WRONGFUL.getCode(), ErrorCode.TOKEN_WRONGFUL.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(user.getId());
        loginUserVo.setNickname(user.getNickname());
        loginUserVo.setAccount(user.getAccount());
        loginUserVo.setAvatar(user.getAvatar());

        return Result.success(loginUserVo);
    }

    /**
     * 查询用户信息
     * @param account
     * @return
     */
    @Override
    public SysUser findByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit "+1);
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 保存用户信息
     * @param sysUser
     * @return
     */
    @Override
    public int save(SysUser sysUser) {
        // 注意 默认生成的id 是分布式id 采用了雪花算法
        return sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findAuthorById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if (sysUser == null) {
            SysUser user = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("码神之路");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser, userVo);
        return userVo;
    }


}
