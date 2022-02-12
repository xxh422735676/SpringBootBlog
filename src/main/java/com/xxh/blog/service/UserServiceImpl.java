package com.xxh.blog.service;

import com.xxh.blog.dao.UserRepository;
import com.xxh.blog.dbU.User;
import com.xxh.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {

        User user=userRepository.findByUsernameAndPassword(username, MD5Util.code(password));
        return user;
    }
}
