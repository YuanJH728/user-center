package com.yupi.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author yjh
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 账户
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 校验码
     */
    private String checkPassword;

    /**
     *
     */
    private String planetCode;
}
