package org.chatapp.controllers;

import org.chatapp.services.SocialUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

@Controller
public class FacebookController {

    @Autowired
    private SocialUserService socialUserService;

    private final Facebook facebook;
    private final ConnectionRepository connectionRepository;

    public FacebookController(Facebook facebook,
                              ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping("/facebook/connected")
    public String getFacebookDetails(HttpSession session, Model model) throws IOException {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

        ConnectionKey connectionKey = this.connectionRepository.findPrimaryConnection(Facebook.class).getKey();
        String [] fields = { "id","name","email","location","gender","first_name","last_name"};
        User fbUser = facebook.fetchObject("me", User.class, fields);
        byte[] profilePicture = facebook.userOperations().getUserProfileImage();
        String image = "data:image/png;base64," + Base64.getEncoder().encodeToString(profilePicture);
        this.socialUserService.proceedFbUser(fbUser,image);
        this.connectionRepository.removeConnection(connectionKey);

        return "redirect:/";
    }


}

