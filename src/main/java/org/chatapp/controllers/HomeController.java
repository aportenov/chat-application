package org.chatapp.controllers;

import org.chatapp.messages.Errors;
import org.chatapp.models.UserBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class HomeController {


    @GetMapping("/")
    public String getPage(){
        return "redirect:/chat";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error,
                               Model model) {
        if (!model.containsAttribute("user")) {
             model.addAttribute("user", new UserBindingModel());
        }

        if(error != null){
            model.addAttribute("error", Errors.INVALID_CREDENTIALS);
        }

        return "login";
    }

    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }
}
