package com.svanty.module.blog.controller.admin;

import com.svanty.constans.GlobalConstants;
import com.svanty.module.core.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("{locale}")
public class BlogAdminController {

    private MembersService membersService;

    @Autowired
    BlogAdminController(MembersService membersService) {
        this.membersService = membersService;
    }


    @GetMapping("/panel/admin/blog")
    public String index(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                        @RequestParam(value = "size", required = false, defaultValue = GlobalConstants.PAGINATION_CURRENT_ITEMS_COUNT) int size, Model model) {

//        Paged<User> users = membersService.getPage(pageNumber, size);
//        Long count = membersService.count();
//        model.addAttribute("users", users);
//        model.addAttribute("userCount", count);

        return "panel/admin/member/members";
    }


    @GetMapping("/panel/admin/blog/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {

//        User user = membersService.getById(id);
//
//        model.addAttribute("user", user);

        return "panel/admin/member/edit";
    }


}
