package com.longrise.msaas.service;

import com.longrise.msaas.global.domain.EntityBean;

public interface WeChatService {
  EntityBean getAccessToken(String url);
}
