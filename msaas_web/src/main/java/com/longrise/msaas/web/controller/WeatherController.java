package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherController {
  public final String DATA_TYPE = "now"; // all/now
  public final String AK = "awI5x1uR80p7Zgcr3cpzdTQWeii5xZCm";
  public final String URL = "https://api.map.baidu.com/weather/v1/?district_id={district_id}&data_type={data_type}&ak={ak}";

  @Autowired
  private RestTemplate restTemplate;

  @CrossOrigin
  @GetMapping("/weather")
  public String getWeather(@RequestParam String district_id) {
    EntityBean bean = new EntityBean();
    bean.put("district_id", district_id);
    bean.put("data_type", this.DATA_TYPE);
    bean.put("ak", this.AK);
    return this.restTemplate.getForObject(URL, String.class, bean);
  }
}
