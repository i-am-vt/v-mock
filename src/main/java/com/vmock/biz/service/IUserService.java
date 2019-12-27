package com.vmock.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmock.biz.entity.User;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author mock
 */
public interface IUserService extends IService<User> {

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUserList(User user);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    User selectUserByLoginName(String userName);

    /**
     * 修改用户密码信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean resetUserPwd(User user);

    /**
     * 校验用户名称是否唯一
     *
     * @param loginName 登录名称
     * @return 结果
     */
    Boolean checkLoginNameUnique(String loginName);


    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    void checkUserAllowed(User user);

    /**
     * 用户注册
     *
     * @param username       用户名
     * @param invitationCode 邀请码
     * @param password       密码
     * @return 是否注册成功
     */
    boolean register(String username, String invitationCode, String password);
}
