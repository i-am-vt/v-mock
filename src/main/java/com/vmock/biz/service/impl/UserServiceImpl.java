package com.vmock.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.base.constant.CommonConst;
import com.vmock.base.exception.BusinessException;
import com.vmock.base.login.PasswordService;
import com.vmock.biz.entity.User;
import com.vmock.biz.mapper.UserMapper;
import com.vmock.biz.service.IConfigService;
import com.vmock.biz.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author mock
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private IConfigService configService;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<User> selectUserList(User user) {
        Long beginTime = Convert.toLong(user.getParams().get("beginTime"));
        Long endTime = Convert.toLong(user.getParams().get("endTime"));
        return this.list(Wrappers.<User>lambdaQuery()
                // like login nameF
                .like(StrUtil.isNotBlank(user.getLoginName()), User::getLoginName, user.getLoginName())
                // equals status
                .eq(user.getStatus() != null, User::getStatus, user.getStatus())
                // like phone
                .like(StrUtil.isNotBlank(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber())
                // beginTime
                .ge(beginTime != null, User::getCreateTime, beginTime)
                // endTime 加入当日的时间戳
                .le(endTime != null, User::getCreateTime, endTime));
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public User selectUserByLoginName(String userName) {
        return this.getOne(Wrappers.<User>lambdaQuery().eq(User::getLoginName, userName).last(CommonConst.LIMIT_ONE));
    }


    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean resetUserPwd(User user) {
        user.randomSalt();
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return this.updateById(user);
    }

    /**
     * 校验登录名称是否唯一
     *
     * @param loginName 用户名
     * @return
     */
    @Override
    public Boolean checkLoginNameUnique(String loginName) {
        int count = this.count(Wrappers.<User>lambdaQuery().eq(User::getLoginName, loginName));
        return count == 0;
    }


    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(User user) {
        if (CommonConst.ADMIN_ID.equals(user.getUserId())) {
            throw new BusinessException("不能修改最终管理员的状态");
        }
    }

    /**
     * 用户注册
     *
     * @param username       用户名
     * @param invitationCode 邀请码
     * @param password       密码
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean register(String username, String invitationCode, String password) {
        // check invitation code
        String systemCode = configService.getVal("system.invitation.code");
        if (!StrUtil.equals(systemCode, invitationCode)) {
            throw new BusinessException("邀请码不正确！");
        }
        if (!this.checkLoginNameUnique(username)) {
            throw new BusinessException("账号已存在！");
        }
        User user = new User();
        user.setLoginName(username);
        user.setPassword(password);
        user.setUserName(username);
        user.randomSalt();
        // 密码处理
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        // 新增用户
        return this.save(user);
    }
}
