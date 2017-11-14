package org.chatapp.controllers;

import org.chatapp.entities.Room;
import org.chatapp.models.RoomModel;
import org.chatapp.models.UserBindingModel;
import org.chatapp.services.RoomService;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getPage(){
//       User user = new User();
//       user.setFullName("Testov User");
//       user.setUsername("user1");
//       user.setPassword("1");
//       this.userService.save(user);
        return "index";
    }

    @PostMapping("/register")
    public String createRoom(@ModelAttribute UserBindingModel userBindingModel){
       this.userService.save(userBindingModel);
        return "chat";
    }
}
