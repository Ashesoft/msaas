package com.longrise.msaas.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrController {
    @GetMapping("/err")
    public int err(){
        int a = 1/0;
        return a;
    }
}
