package com.vmock.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author mock
 */
@Data
@TableName("sys_menu")
public class Menu extends BaseEntity<Menu> {

    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 普通用户可见 ： 1全部可见 0 管理员可见
     */
    private Integer userDisplay;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    @NotBlank(message = "显示顺序不能为空")
    private Integer orderNum;

    /**
     * 菜单URL
     */
    @Size(min = 0, max = 200, message = "请求地址不能超过200个字符")
    private String url;

    /**
     * 打开方式：menuItem页签 menuBlank新窗口
     */
    private String target;

    /**
     * 类型:0目录,1菜单,2按钮
     */
    @NotBlank(message = "菜单类型不能为空")
    private Integer menuType;

    /**
     * 菜单状态:0显示,1隐藏
     */
    private Integer visible;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

}
