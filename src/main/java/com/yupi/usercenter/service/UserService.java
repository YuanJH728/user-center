package com.yupi.usercenter.service;

import com.yupi.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务
* @author yjh
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-02-14 11:52:23
*/
public interface UserService extends IService<User> {


    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 返回脱敏后用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     * @param user
     * @return
     */
    User getSafetyUser(User user);


    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

}
