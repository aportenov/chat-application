package org.chatapp.controllers;


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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;


@Controller
public class ChatController {

    private Principal principal;

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;


    @MessageMapping("/sendMessage/{room}")
    @SendTo("/topic/room/{room}")
    public MessageModel chatRoom(@DestinationVariable String room,
                                 @Payload MessageModel message) {
        message.setUser(this.principal.getName());
        this.messageService.addMessage(message);
        return message;
    }

    @MessageMapping("/addUser/{room}")
    @SendTo("/topic/room/{room}")
    public MessageModel addUser(@DestinationVariable String room,
                              @Payload MessageModel message,
                             SimpMessageHeaderAccessor headerAccessor) {
        this.principal = headerAccessor.getUser();
        message.setUser(this.principal.getName());
        this.messageService.addMessage(message);

        return message;
    }

    @MessageMapping("/addRoom/{room}")
    @SendTo("/topic/room/{room}")
    public MessageModel addRooms(@DestinationVariable String room,
                              @Payload MessageModel message) {

        message.setUser(this.principal.getName());
        this.messageService.addMessage(message);

        return message;
    }
}