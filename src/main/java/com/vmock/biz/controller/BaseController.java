package com.vmock.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vmock.base.login.UserContext;
import com.vmock.base.vo.PageVo;
import com.vmock.base.vo.Result;
import com.vmock.base.vo.TableDataVo;
import com.vmock.base.vo.TableSupport;
import com.vmock.biz.entity.User;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author mock
 */
public class BaseController {

    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    public static final String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";
    private static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @SneakyThrows
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text, PARSE_PATTERNS));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageVo pageVo = TableSupport.getPage();
        Integer pageNum = pageVo.getPageNum();
        Integer pageSize = pageVo.getPageSize();
        if (pageNum != null && pageSize != null) {
            String orderBy = pageVo.getOrderBy().matches(SQL_PATTERN) ? pageVo.getOrderBy() : StrUtil.EMPTY;
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    protected <T> TableDataVo getDataTable(List<T> list) {
        TableDataVo rspData = new TableDataVo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo<>(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected Result create(int rows) {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected Result create(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public Result success() {
        return Result.success();
    }

    /**
     * 返回失败消息
     */
    public Result error() {
        return Result.error();
    }

    /**
     * 返回成功消息
     */
    public Result success(String message) {
        return Result.success(message);
    }

    /**
     * 返回失败消息
     */
    public Result error(String message) {
        return Result.error(message);
    }

    /**
     * 返回错误码消息
     */
    public Result error(Result.Type type, String message) {
        return Result.create(type, message);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return String.format("redirect:{%s}", url);
    }

    public User getSysUser() {
        return UserContext.getSysUser();
    }

    public void setSysUser(User user) {
        UserContext.setSysUser(user);
    }

    public Long getUserId() {
        return getSysUser().getUserId();
    }

    public String getLoginName() {
        return getSysUser().getLoginName();
    }
}
