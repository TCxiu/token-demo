package com.xiu.token.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther 创建者: Tc李
 * @Date 创建时间: 2018/06/28 10:27
 * @Description 类描述:
 */

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("")
    public Object index(){
        return "ok";
    }
}
