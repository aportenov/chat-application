package org.chatapp.controllers;

import org.chatapp.entities.Room;
import org.chatapp.models.RoomModel;
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

    @GetMapping("/test")
    public String getPage(){
//       User user = new User();
//       user.setFullName("Testov User");
//       user.setUsername("user1");
//       user.setPassword("1");
//       this.userService.save(user);
        return "index";
    }

    @PostMapping("/test")
    public String createRoom(@ModelAttribute RoomModel roomModel, ModelMap modelMap){
        Room room = new Room();
        room.setName(roomModel.getName());
        this.roomService.save(room);
        return "room";
    }
}
