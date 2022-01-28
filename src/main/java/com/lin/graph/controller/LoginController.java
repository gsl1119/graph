package com.lin.graph.controller;

import com.lin.graph.common.aop.LogAnnotation;
import com.lin.graph.service.LoginService;
import com.lin.graph.vo.Result;
import com.lin.graph.vo.params.LoginParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("login")
public class LoginController {
    @Resource
    private LoginService loginService;
    @PostMapping
    @LogAnnotation(module="登录",operation="获取登录信息")
    public Mono<Result> login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
