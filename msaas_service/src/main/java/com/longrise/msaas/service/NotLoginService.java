package com.longrise.msaas.service;


import com.longrise.msaas.global.domain.EntityBean;

public interface NotLoginService {
  EntityBean[] queryPhoneAreaCode();

  EntityBean isOwnerUserByPhone(EntityBean bean);
}
