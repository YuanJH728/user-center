package com.yupi.usercenter.exception;

import com.yupi.usercenter.common.ErrorCode;

/**
 * 自定义异常类
 * @author yjh
 */
public class BusinessException extends RuntimeException {

    private int code;
    private String description;
    public BusinessException(String msg,int code,String description) {
        super(msg);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
