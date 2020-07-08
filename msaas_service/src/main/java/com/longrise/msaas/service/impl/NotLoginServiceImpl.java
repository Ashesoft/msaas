package com.longrise.msaas.service.impl;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.mapping.NotLoginMapping;
import com.longrise.msaas.service.NotLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotLoginServiceImpl implements NotLoginService {

    @Autowired
    private NotLoginMapping notLoginMapping;

    @Override
    public EntityBean[] queryPhoneAreaCode() {
        return notLoginMapping.queryPhoneAreaCode();
    }

    @Override
    public EntityBean isOwnerUserByPhone(EntityBean bean) {
        return notLoginMapping.isOwnerUserByPhone(bean);
    }
}
