package com.longrise.msaas.service;

import com.longrise.msaas.global.domain.EntityBean;

public interface StoryService {
    /**
     * 获取故事列表
     * @return
     */
    EntityBean[] getBookList();

    /**
     * 获取对应章回内容
     * @return 列表
     */
    EntityBean getBookContent(Long sid);
}
