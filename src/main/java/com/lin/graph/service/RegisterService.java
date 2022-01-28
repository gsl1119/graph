package com.lin.graph.service;

import com.lin.graph.vo.Result;
import com.lin.graph.vo.params.RegisterParam;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Transactional // 事务，此事务若出错则不会发生任何改变，即无法写入数据库
public interface RegisterService {
    Mono<Result> register(RegisterParam registerParam);
}
