package org.chatapp.controllers;


import org.chatapp.entities.AbstractUser;
import org.chatapp.entities.SocialUser;
import org.chatapp.enumerable.MessageType;
import org.chatapp.models.MessageModel;
import org.chatapp.models.RoomModel;
import org.chatapp.services.MessageService;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;


@Controller
public class ChatController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;


    @MessageMapping("/sendMessage/{room}")
    @SendTo("/topic/room/{room}")
    public MessageModel chatRoom(@DestinationVariable String room,
                                 @Payload MessageModel message,
                                 SimpMessageHeaderAccessor headerAccessor) {
        UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) headerAccessor.getUser();
        if (principal.getPrincipal() instanceof AbstractUser) {
            AbstractUser user = (AbstractUser) principal.getPrincipal();
            message.setImage(user.getImage());
        }

        message.setUser(principal.getName());
        message.setMessageType(String.valueOf(MessageType.CHAT));
        message.setRoomName(room);
        this.messageService.addMessage(message);

        return message;
    }

    @MessageMapping("/addRoom/{room}")
    @SendTo("/topic/room/{room}")
    public MessageModel addRoom(@DestinationVariable String room,
                                @Payload MessageModel message,
                                SimpMessageHeaderAccessor headerAccessor) {

        Principal principal = headerAccessor.getUser();
        message.setUser(principal.getName());
        message.setMessageType(String.valueOf(MessageType.JOIN));
        message.setMessage(principal.getName() + " has joined the channel");
        message.setRoomName(room);
        this.userService.addRoom(principal.getName(), room);

        return message;
    }

    @MessageMapping("/leaveRoom/{room}")
    @SendTo("/topic/room/{room}")
    public MessageModel leaveRoom(@DestinationVariable String room,
                                  SimpMessageHeaderAccessor headerAccessor) {
        Principal principal = headerAccessor.getUser();
        this.userService.leaveRoom(principal.getName(), room);
        MessageModel message = new MessageModel();
        message.setUser(principal.getName());
        message.setMessageType(String.valueOf(MessageType.LEAVE));
        message.setMessage(principal.getName() + " has left the channel");
        message.setRoomName(room);

        return message;
    }
}