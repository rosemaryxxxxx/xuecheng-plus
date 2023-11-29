package com.xuecheng.content.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/22 16:59
 * @PackageName:com.xuecheng.content.api
 * @ClassName: FreemarkerController
 * @Description: freemarker测试
 * @Version 1.0
 */
@Controller
public class FreemarkerController {
    @GetMapping("/testfreemarker")
    public ModelAndView test(){
        ModelAndView modelAndView = new ModelAndView();
        //设置模型数据
        modelAndView.addObject("name1","小明");
        //设置模板名称
        modelAndView.setViewName("test");
        return modelAndView;
    }
}
