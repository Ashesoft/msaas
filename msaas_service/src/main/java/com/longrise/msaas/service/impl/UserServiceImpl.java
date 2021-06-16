package com.longrise.msaas.service.impl;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.utils.BindLogClass;
import com.longrise.msaas.mapping.UserMapping;
import com.longrise.msaas.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
  @Resource
  private UserMapping userMapping;

  public EntityBean[] getAllUserInfo(Integer count) {
    BindLogClass.put(this.getClass().getName());
    return userMapping.getAllUserInfo(count);
  }
}
