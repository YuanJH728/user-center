package com.yupi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenter.common.ErrorCode;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.model.User;
import com.yupi.usercenter.service.UserService;
import com.yupi.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yupi.usercenter.constrain.UserConstant.USER_LOGIN_STATE;

/**
* @author yjh
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-02-14 11:52:23
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 盐
     */
    private static final String SALT = "yupi";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            //todo:修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
//            return -1;
        }
        if(planetCode.length()>5){
//            return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球账号过短");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
//            return -2;
        }
        if(userPassword.length()<8||checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码或校验码长度过短");
//            return -3;
        }
        //不能包含特殊字符
        String validPattern = ".*[[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t].*";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户含有特殊字符");
        }
        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和校验码不同");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if(count>0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户已存在");
        }

        //星球编号不能重复
        queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("planetCode", planetCode);
        count = this.count(queryWrapper);
        if(count>0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号已经存在");
        }

        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        System.out.println(encryptPassword);
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean result = this.save(user);
        if(!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度太短");
        }
        if(userPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度太短");
        }
        //不能包含特殊字符
        String validPattern = ".*[[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t].*";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户含有特殊字符");
        }

        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if(user==null) {
            log.info("user login failed,userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }

        User safetyUser = getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    @Override
    public User getSafetyUser(User user){
        if(user==null){
            return null;
        }
        //脱敏
        User safeuser = new User();
        safeuser.setId(user.getId());
        safeuser.setUsername(user.getUsername());
        safeuser.setUserAccount(user.getUserAccount());
        safeuser.setAvatarUrl(user.getAvatarUrl());
        safeuser.setUserRole(user.getUserRole());
        safeuser.setPlanetCode(user.getPlanetCode());
        safeuser.setGender(user.getGender());
        safeuser.setPhone(user.getPhone());
        safeuser.setEmail(user.getEmail());
        safeuser.setUserStatus(user.getUserStatus());
        safeuser.setCreateTime(user.getCreateTime());
        return safeuser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




