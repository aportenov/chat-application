package org.chatapp.controllers;

import org.chatapp.models.MessageModel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

@org.springframework.stereotype.Controller
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
        headerAccessor.getSessionAttributes().put("username", "Anton");//message.getUser().getUsername());
        return message;
    }
}
