package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AudioController {

  @Autowired
  private AudioService audioService;

  @GetMapping("/getAudios")
  public EntityBean[] getAudios() {
    return audioService.getAllAudio();
  }
}
