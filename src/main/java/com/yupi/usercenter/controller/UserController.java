package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.usercenter.common.BaseResponse;
import com.yupi.usercenter.common.ErrorCode;
import com.yupi.usercenter.common.ResultUtils;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.model.User;
import com.yupi.usercenter.model.request.UserLoginRequest;
import com.yupi.usercenter.model.request.UserRegisterRequest;
import com.yupi.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.yupi.usercenter.constrain.UserConstant.ADMIN_ROLE;
import static com.yupi.usercenter.constrain.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author yjh
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    /**
     * @RequestBody让userRegisterRequest和前端对应上
     */
    public BaseResponse<Long> UserRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        return ResultUtils.success(id);
//        return new BaseResponse<>(0,"ok",id);
    }

    @PostMapping("/login")
    /**
     * @RequestBody让userRegisterRequest和前端对应上
     */
    public BaseResponse<User> UserLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogin(userAccount, userPassword, request));
//        return new BaseResponse<>(0,"ok",userService.userLogin(userAccount, userPassword, request));
    }

    @GetMapping("/search")
    /**
     * @RequestBody让userRegisterRequest和前端对应上
     */
    public BaseResponse<List<User>> searchUser(String userName,HttpServletRequest request) {
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
//            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            queryWrapper.like("username", userName);
        }
        List<User> userList = userService.list(queryWrapper);

        //返回脱敏信息
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
        //return userService.list(queryWrapper);
    }

    @PostMapping("/delete")
    /**
     * @RequestBody让userRegisterRequest和前端对应上
     */
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if(id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //todo校验用户是否合法
        long id = currentUser.getId();
        User user = userService.getById(id);
        User safetyuser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyuser);
//        return userService.getSafetyUser(user);
    }
    /**
     * 是否是管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //管理员才能查找
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> UserLogout(HttpServletRequest request) {
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
//        return userService.userLogout(request);
    }
}
