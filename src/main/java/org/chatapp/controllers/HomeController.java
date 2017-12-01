package org.chatapp.controllers;
;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class HomeController {


    @GetMapping("/")
    public String getPage(){
        return "redirect:/chat";
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
