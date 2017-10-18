package com.work.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.beans.User;
import com.work.dao.UserMapper;
import com.work.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	public User findUserByToKenId(String toKenId) {
		User user = userMapper.findUserByToKenId(toKenId);
		return user;
	}
	
}
