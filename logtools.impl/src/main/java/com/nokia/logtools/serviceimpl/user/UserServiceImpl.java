package com.nokia.logtools.serviceimpl.user;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.nokia.logtools.dao.user.UserDao;
import com.nokia.logtools.dbo.UserBO;
import com.nokia.logtools.domain.user.User;
import com.nokia.logtools.service.user.UserService;


@Service("userService")
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;
	
	@Override
	public UserBO queryObject(Integer id){
		UserBO userBean = null;
		User user =  userDao.getById(id);
			if( user != null ) {
			userBean = new UserBO();
			BeanUtils.copyProperties( user, userBean );
		}
		return userBean;
	}
	

	
	
}
