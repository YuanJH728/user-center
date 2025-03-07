package com.yupi.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 账户
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;


}
