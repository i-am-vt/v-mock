package com.vmock.base.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异步操作的返回包装
 *
 * @author mock
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

    /**
     * 应用对接码
     */
    private Integer code;

    /**
     * 文字消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     */
    public static Result create(Type type, String msg) {
        return create(type, msg, null);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public static <T> Result create(Type type, String msg, T data) {
        Result result = new Result();
        result.code = type.value;
        result.msg = msg;
        result.data = data;
        return result;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static Result success() {
        return Result.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> Result success(T data) {
        return Result.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static Result success(String msg) {
        return Result.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> Result success(String msg, T data) {
        return Result.create(Type.SUCCESS, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result warn(String msg) {
        return Result.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result warn(String msg, Object data) {
        return Result.create(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static Result error() {
        return Result.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result error(String msg) {
        return Result.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result error(String msg, Object data) {
        return Result.create(Type.ERROR, msg, data);
    }

    /**
     * 状态类型
     */
    @AllArgsConstructor
    public enum Type {
        /**
         * 成功
         */
        SUCCESS(0),
        /**
         * 警告
         */
        WARN(301),
        /**
         * 错误
         */
        ERROR(500);

        private final int value;

        public int value() {
            return this.value;
        }
    }
}
