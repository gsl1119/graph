//package com.lin.graph.router;
//
//import com.lin.graph.controller.LoginController;
//import com.lin.graph.vo.Result;
//import org.springframework.http.MediaType;
//import org.springframework.web.servlet.function.RouterFunction;
//import org.springframework.web.servlet.function.RouterFunctions;
//import org.springframework.web.servlet.function.ServerResponse;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
//import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
//
//public class Router {
//    public RouterFunction<ServerResponse> routerFunction(){
//        return RouterFunctions.route(POST("/login").and(accept(APPLICATION_JSON)), LoginController::login);
//    }
//}
