package com.spring.controller;

import com.spring.annoation.Controller;
import com.spring.annoation.RequestMapping;

@Controller
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/testMethod")
    public String test(){
        return "test SpringMVC success";
    }
}
