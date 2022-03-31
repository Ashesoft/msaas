package com.longrise.msaas.service;

import com.longrise.msaas.global.domain.EntityBean;

public interface UserService {
  EntityBean[] getAllUserInfo(Integer count);
}
