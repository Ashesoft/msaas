package com.longrise.msaas.service.impl;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.mapping.StoryMapping;
import com.longrise.msaas.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryMapping storyMapping;

    @Cacheable(value = "booklist", keyGenerator = "myKeyGenerator")
    @Override
    public EntityBean[] getBookList() {
        return storyMapping.getBookList();
    }

    @Cacheable(value = "bookcatalog", key = "#root.methodName + '[' + #bid + ']'")
    @Override
    public EntityBean getBookCatalog(Long bid) {
        EntityBean bean = new EntityBean(1);
        bean.put("desc", storyMapping.getBookDescribe(bid));
        bean.put("catalogs", storyMapping.getBookCatalog(bid));
        return bean;
    }

    @Override
    public EntityBean getBookContent(Long sid) {
        return storyMapping.getBookContent(sid);
    }
}
