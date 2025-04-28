package com.nikita.shop.http.controller;

import com.nikita.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin-main")
@RequiredArgsConstructor
public class AdminPanelController {
    private final UserService userService;

    @GetMapping
    public String adminMain(Model model) {
        return "admin-main";
    }


}
