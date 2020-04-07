package com.example.layui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layui")
public class LayuiController {
     
    @RequestMapping("/index")
    public String demo() {
        return "layui/index";
    }
  
}