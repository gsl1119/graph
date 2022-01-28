package com.lin.graph.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.graph.dao.mapper.UserMapper;
import com.lin.graph.dao.pojo.User;
import com.lin.graph.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User findUser( String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        System.out.println(username);
        System.out.println(password);
        queryWrapper.eq(User::getUsername,username);
        queryWrapper.eq(User::getPassword,password);
        queryWrapper.select(User::getId, User::getUsername,User::getPassword);
        queryWrapper.last("limit 1");
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public User findUserByAccount(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        queryWrapper.last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(User user) {
        this.userMapper.insert(user);
    }


}