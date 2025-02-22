package com.yupi.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 * @author yjh
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    private String description;

    public BaseResponse() {
    }

    public BaseResponse(int code, String msg, T data, String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(int code, T data, String description) {
        this(code, "", data, description);
    }

    public BaseResponse(int code, T data) {
        this(code, "", data, "");
    }

    public BaseResponse(ErrorCode msg) {
        this(msg.getCode(), msg.getMessage(), null, msg.getDescription());
    }

}
