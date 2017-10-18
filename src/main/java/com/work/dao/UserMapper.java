package com.work.dao;

import org.springframework.stereotype.Repository;

import com.work.beans.User;

@Repository
public interface UserMapper {
	User findUserByToKenId(String tokenId);
}
