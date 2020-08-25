package com.longrise.msaas.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.service.NotLoginService;
import com.longrise.msaas.web.configuration.JsonWebTokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

//@RestController
@Controller
public class NotLoginController {

    @Autowired
    private NotLoginService notLoginService;

    @Autowired
    private JsonWebTokenConfig jsonWebTokenConfig;

    @ResponseBody
    @PostMapping("/ulogin")
    public EntityBean index(@RequestParam Map<String, Object> bean) {
        EntityBean bean1 = notLoginService.isOwnerUserByPhone(Stream.of(bean).collect(EntityBean::new, EntityBean::putAll, EntityBean::putAll));
        String token = jsonWebTokenConfig.generateToken(bean1.getString("uphone"));
        bean1.put("token", token);
        return bean1;
    }

    @ResponseBody
    @PostMapping("/thome")
    public void home(String json) {
        try {
            EntityBean bean = new ObjectMapper().readValue(json, EntityBean.class);
            System.out.println(bean.toJsonString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getHtmlTemp")
    public String getHtmlTemp() {
        return "/htemp";
    }

    @ResponseBody
    @PostMapping("/queryPhoneAreaCode")
    public EntityBean[] queryPhoneAreaCode() {
        return notLoginService.queryPhoneAreaCode();
    }
}
