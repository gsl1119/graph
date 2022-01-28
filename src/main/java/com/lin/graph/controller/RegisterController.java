package com.lin.graph.controller;

import com.lin.graph.service.RegisterService;
import com.lin.graph.vo.Result;
import com.lin.graph.vo.params.RegisterParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Resource // 根据属性类型进行自动装配
    private RegisterService registerService;

    @RequestMapping
    public Mono<Result> register(@RequestBody RegisterParam registerParam){
        return registerService.register(registerParam);
    }
}
