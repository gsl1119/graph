package com.lin.graph.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lin.graph.dao.pojo.User;
import com.lin.graph.service.RegisterService;
import com.lin.graph.service.UserService;
import com.lin.graph.utils.JWTUtils;
import com.lin.graph.vo.ErrorCode;
import com.lin.graph.vo.Result;
import com.lin.graph.vo.params.RegisterParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static com.lin.graph.service.impl.LoginServiceImpl.slat;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Result> register(RegisterParam registerParam) {
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        User user = this.userService.findUserByAccount(username);
        if (user != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(DigestUtils.md5Hex(password+slat));
        this.userService.save(user);

        //token
        String token = JWTUtils.createToken((long) user.getId());

//        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
