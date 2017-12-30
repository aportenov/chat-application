package org.chatapp.controllers;

import org.chatapp.models.UserBindingModel;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;


@Controller
public class UserController {

    private static final String BASE64_PNG = "data:image/png;base64,";

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute UserBindingModel userBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);;
            redirectAttributes.addFlashAttribute("user", userBindingModel);

            return "redirect:/login";
        }

        if (!file.isEmpty()) {
            String image = BASE64_PNG + Base64.getEncoder().encodeToString(file.getBytes());
            userBindingModel.setImage(image);
        }

        this.userService.save(userBindingModel);
        return "redirect:/";
    }
}
