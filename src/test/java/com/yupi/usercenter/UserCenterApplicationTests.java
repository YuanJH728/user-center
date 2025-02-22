package com.yupi.usercenter;

import com.yupi.usercenter.model.User;
import com.yupi.usercenter.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@MapperScan("com.yupi.usercenter.mapper")
class UserCenterApplicationTests {

	@Resource
	private UserService userService;

	@Test
	void contextLoads() {

	}

	@Test
	public  void testAddUser(){
		User user = new User();
		user.setUserAccount("dogyupi");
		user.setUsername("test");
		user.setAvatarUrl("https://www.codefather.cn/course");
		user.setGender(0);
		user.setUserPassword("123456");
		user.setPhone("123456789");
		user.setEmail("123456@qq.com");
		System.out.println(user.getId());
		boolean result =  userService.save(user);
		System.out.println(user.getId());
		Assertions.assertTrue(result);
	}

	@Test
	public void userRegister(){
		String userAccount = "test";
		String userPassword = "";
		String checkPassword = "12345678";
		String planetCode  = "1";
		long result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
		Assertions.assertEquals(-1,result);

		userAccount = "te*$";
		userPassword = "12345678";
		result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
//		Assertions.assertEquals(-1,result);

		userAccount = "test";
		userPassword = "123456";
		result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
//		Assertions.assertEquals(-1,result);

		userAccount = "te t";
		userPassword = "12345678";
		result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
//		Assertions.assertEquals(-1,result);

		userAccount = "test";
		checkPassword = "123456789";
		result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
//		Assertions.assertEquals(-1,result);

		userAccount = "dogyupi";
		checkPassword = "12345678";
		result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
//		Assertions.assertEquals(-1,result);

		userAccount = "yuyuan";
		result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
		System.out.println(result);
//		Assertions.assertTrue(result>0);
	}
}
