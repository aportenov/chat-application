package org.chatapp.controllers;

import org.chatapp.entities.User;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


@Controller
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/user/rooms")
    public ResponseEntity<List<String>> getRooms() {
        String[] rooms;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();
            rooms = this.userService.findRoomsByUser(currentUser.getUsername());
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }

        ResponseEntity<List<String>> responseEntity
                = new ResponseEntity(rooms, HttpStatus.OK);

        return responseEntity;
    }


}
