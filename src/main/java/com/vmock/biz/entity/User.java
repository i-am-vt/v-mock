package com.vmock.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vmock.base.annotation.Excel;
import com.vmock.base.annotation.Excel.ColumnType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户对象 sys_user
 *
 * @author mock
 */
@Data
@TableName("sys_user")
@RequiredArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<User> {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    @NonNull
    private Long userId;

    /**
     * 登录名称
     */
    @Excel(name = "登录名称")
    @NotBlank(message = "登录账号不能为空")
    @Size(max = 30, message = "登录账号长度不能超过30个字符")
    private String loginName;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    private String userName;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    private String phonenumber;

    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private Integer sex;

    /**
     * 用户类型
     *
     * @see {@com.vmock.biz.enums.UserTypeEnum}
     */
    private Integer userType;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐加密
     */
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private Integer status;

    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Long loginDate;

    /**
     * 生成随机盐
     */
    public void randomSalt() {
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        String hex = secureRandom.nextBytes(3).toHex();
        setSalt(hex);
    }
}
