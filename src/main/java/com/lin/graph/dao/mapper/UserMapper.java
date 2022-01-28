package com.lin.graph.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.graph.dao.pojo.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    List<User> findUser(String username);
}
