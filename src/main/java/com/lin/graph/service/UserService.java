package com.lin.graph.service;


import com.lin.graph.dao.pojo.User;


public interface UserService {

    User findUser(String username, String password);

    User findUserByAccount(String username);

    void save(User user);
}
