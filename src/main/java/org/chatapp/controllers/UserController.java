package org.chatapp.controllers;

import org.chatapp.entities.User;
import org.chatapp.models.UserBindingModel;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@ModelAttribute UserBindingModel userBindingModel){
        this.userService.save(userBindingModel);
        return "redirect:/";
    }
}
