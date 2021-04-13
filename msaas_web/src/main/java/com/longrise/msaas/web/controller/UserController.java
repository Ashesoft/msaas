package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.annotation.LoginToken;
import com.longrise.msaas.global.annotation.PassToken;
import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PassToken
    @GetMapping("/getusers/{count}")
    public EntityBean[] getAllUserInfo(@PathVariable("count") Integer count){
        return userService.getAllUserInfo(count);
    }

    @LoginToken
    @GetMapping("/testmsg")
    public String show() {
        return "这是一个测试信息";
    }
}
