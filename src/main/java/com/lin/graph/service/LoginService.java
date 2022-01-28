package com.lin.graph.service;

import com.lin.graph.dao.pojo.User;
import com.lin.graph.vo.Result;
import com.lin.graph.vo.params.LoginParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<Result> login(LoginParam loginParam);

    Flux<User> checkToken(String token);
}
