package com.vmock.base.exception;

import lombok.AllArgsConstructor;

/**
 * 业务异常
 *
 * @author mock
 */
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    protected final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
