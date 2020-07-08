package com.longrise.msaas.service.impl;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.mapping.AudioMapping;
import com.longrise.msaas.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AudioServiceImpl implements AudioService {
    @Autowired
    private AudioMapping audioMapping;

    public EntityBean[] getAllAudio() {
        return audioMapping.getAllAudio();
    }
}
