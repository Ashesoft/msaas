package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.service.NotLoginService;
import com.longrise.msaas.web.configuration.JsonWebTokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//@RestController
@Controller
public class NotLoginController {

    @Autowired
    private NotLoginService notLoginService;

    @Autowired
    private JsonWebTokenConfig jsonWebTokenConfig;

    @ResponseBody
    @PostMapping("/ulogin")
    public EntityBean index(String uphone, String upassword) {
        EntityBean bean = new EntityBean(1);
        try{
            bean.put("uphone", URLEncoder.encode(uphone, "UTF-8"));
        }catch (UnsupportedEncodingException e){
            throw new APIException(5000, "参数编码解码失败");
        }
        bean.put("upassword", upassword);
        EntityBean bean1 = notLoginService.isOwnerUserByPhone(bean);
        String token = jsonWebTokenConfig.generateToken((String) bean1.get("uphone"));
        bean1.put("token", token);
        return bean1;
    }

    @GetMapping("/getHtmlTemp")
    public String getHtmlTemp(){
        return "/htemp";
    }

    @ResponseBody
    @PostMapping("/queryPhoneAreaCode")
    public EntityBean[] queryPhoneAreaCode(){
        return notLoginService.queryPhoneAreaCode();
    }
}
