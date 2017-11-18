package org.chatapp.controllers;

import org.chatapp.entities.User;
import org.chatapp.models.MessageModel;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {


    @MessageMapping("/chat.sendMessage/{roomName}")
    @SendTo("/room/{roomName}")
    public MessageModel chatRoom(@DestinationVariable String roomName,
                                 @Payload MessageModel message) {
        return message;
    }

    @MessageMapping("/chat.addUser/{roomName}")
    @SendTo("/room/{roomName}")
    public MessageModel addUser(@DestinationVariable  String roomName,
                                @Payload MessageModel message,
                                SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session from session/logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        headerAccessor.getSessionAttributes().put("username","Anton" );//message.getUser().getUsername());
        return message;
    }
}
