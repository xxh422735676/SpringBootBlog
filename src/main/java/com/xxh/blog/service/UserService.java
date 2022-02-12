package com.xxh.blog.service;

import com.xxh.blog.dbU.User;

public interface UserService {
    User checkUser(String username, String password);
}
