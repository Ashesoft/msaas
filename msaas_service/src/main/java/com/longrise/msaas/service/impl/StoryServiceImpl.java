package com.longrise.msaas.service.impl;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.mapping.StoryMapping;
import com.longrise.msaas.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryMapping storyMapping;

    @Override
    public EntityBean[] getBookList() {
        return storyMapping.getBookList();
    }

    @Override
    public EntityBean getBookContent(Long sid) {
        return storyMapping.getBookContent(sid);
    }
}
