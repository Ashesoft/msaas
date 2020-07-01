package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/book")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @GetMapping("/list")
    public ModelAndView getBookList(){
        EntityBean[] beans = storyService.getBookList();
        ModelAndView mav = new ModelAndView();
        mav.addObject("list", beans);
        mav.setViewName("/story/hlm/list.html");
        return mav;
    }

    @GetMapping("/content/{sid}")
    public ModelAndView getTitleContent(@PathVariable("sid") Long sid){
        EntityBean bean = storyService.getBookContent(sid);
        ModelAndView mav = new ModelAndView();
        mav.addObject("content", bean);
        mav.setViewName("/story/hlm/hlm.html");
        return mav;
    }
}
