package com.lin.graph.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lin.graph.dao.pojo.User;
import com.lin.graph.service.LoginService;
import com.lin.graph.service.UserService;
import com.lin.graph.utils.JWTUtils;
import com.lin.graph.vo.ErrorCode;
import com.lin.graph.vo.Result;
import com.lin.graph.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    public static final String slat = "lin!@#";
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Result> login(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String pwd = DigestUtils.md5Hex(password + slat);
        User user = userService.findUser(username,pwd);
        if (user == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //登录成功，使用JWT生成token，返回token和redis中
        String token = JWTUtils.createToken((long) user.getId());
//        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    @Async("taskExecutor")
    public Flux<User> checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap==null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        Flux<User> user = JSON.parseObject(userJson, (Type) User.class);
        try {
            //睡眠5秒 证明不会影响主线程的使用
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }


}
