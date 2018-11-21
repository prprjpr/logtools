package com.nokia.logtools.controller.user;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.logtools.constants.ErrorCode;
import com.nokia.logtools.dbo.UserBO;
import com.nokia.logtools.result.Result;
import com.nokia.logtools.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Resource
	UserService userService;
	
	
	@PostMapping("/getUser")
	public Result getUser(@RequestParam("id") String id) {
		
		System.out.println("id="+id);
		UserBO userBO = userService.queryObject(Integer.parseInt(id));
		return Result.success(ErrorCode.SUCC_DO.getCode(),userBO);
		
	}

}
