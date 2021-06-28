package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
public class CaptchaController {
  @GetMapping("/captcha")
  public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
    CaptchaUtil.out(request, response);
  }

  @PostMapping("/checkcode")
  @ResponseBody
  public boolean checkCode(HttpServletRequest request, @RequestBody EntityBean bean) {
    if (Objects.isNull(bean) || !bean.containsKey("valicode")) {
      return false;
    }
    return CaptchaUtil.ver(bean.getString("valicode"), request);
  }
}
