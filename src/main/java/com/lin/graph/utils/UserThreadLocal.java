package com.lin.graph.utils;

import com.lin.graph.dao.pojo.User;
import reactor.core.publisher.Flux;

public class UserThreadLocal {
    private UserThreadLocal(){}

    private static final ThreadLocal<Flux<User>> LOCAL = new ThreadLocal<>();

    public static void put(Flux<User> user){
        LOCAL.set(user);
    }
    public static Flux<User> get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}
