package org.chatapp.controllers;

import org.chatapp.entities.AbstractUser;
import org.chatapp.entities.User;
import org.chatapp.models.UserViewModel;
import org.chatapp.services.RoomService;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    private Authentication authentication;

    @GetMapping("/rooms/all")
    public ResponseEntity<String[]> getAllRooms(){
        String[] rooms;
        if (isUserAuhtenticated()) {
            AbstractUser currentUser = (AbstractUser) authentication.getPrincipal();
            rooms = this.roomService.findRoomsByUser(currentUser.getUsername());
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        ResponseEntity<String[]> responseEntity
                = new ResponseEntity(rooms, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/user/room/{currentRoom}")
    public ResponseEntity<List<UserViewModel>> getUsersByRoom(@PathVariable("currentRoom") String currentRoom){
        List<UserViewModel> users = this.roomService.findUsersRoomByName(currentRoom);
        ResponseEntity<List<UserViewModel>> responseEntity = new ResponseEntity(users, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/users/rooms")
    public ResponseEntity<String[]> getUserRooms() {
        String[] rooms;
        if (isUserAuhtenticated()) {
            AbstractUser currentUser = (AbstractUser) authentication.getPrincipal();
            rooms = this.userService.findRoomsByUser(currentUser.getUsername());
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }

        ResponseEntity<String[]> responseEntity
                = new ResponseEntity(rooms, HttpStatus.OK);

        return responseEntity;
    }


    private Boolean isUserAuhtenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.authentication = authentication;
        return authentication.getPrincipal() instanceof AbstractUser;
    }
}
