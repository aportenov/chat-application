package org.chatapp.controllers;

import org.chatapp.models.UserBindingModel;
import org.chatapp.services.RoomService;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class HomeController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getPage(){
        return "redirect:/chat";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserBindingModel userBindingModel){
       this.userService.save(userBindingModel);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, RedirectAttributes redirectAttributes) {
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", "");
        }

        return "login";
    }

    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }
}
