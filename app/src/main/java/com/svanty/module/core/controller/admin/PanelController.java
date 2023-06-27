package com.svanty.module.core.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("{locale}")
public class PanelController {

    @GetMapping("/panel/admin")
    public String index() {

        return "panel/admin/index";
    }
}
