package com.lin.blog.service;

import com.lin.blog.pojo.User;

public interface UserService {
    User checkUser(String username, String password);
}
