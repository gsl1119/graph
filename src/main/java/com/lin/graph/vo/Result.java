package com.lin.graph.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
public class Result  {
    private boolean success;
    private int code;
    private String msg;
    private Object data;
    public static Mono<Result> success(Object data){
        return Mono.justOrEmpty(new Result(true,200,"success",data));
    }
    public static Mono<Result> fail(int code,String msg){
        return Mono.justOrEmpty(new Result(false,code,msg,null));
    }
}
