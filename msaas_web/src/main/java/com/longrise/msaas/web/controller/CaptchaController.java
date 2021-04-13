package com.longrise.msaas.web.controller;

import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CaptchaController {
  @RequestMapping("/captcha")
  public void captcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
    CaptchaUtil.out(request, response);
  }
}
