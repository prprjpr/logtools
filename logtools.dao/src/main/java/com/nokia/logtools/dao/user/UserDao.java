package com.nokia.logtools.dao.user;

import org.apache.ibatis.annotations.Mapper;

import com.nokia.logtools.dao.BaseDao;
import com.nokia.logtools.domain.user.User;

@Mapper
public interface UserDao extends BaseDao<User> {


}
