package org.chatapp.controllers;

import org.chatapp.entities.Message;
import org.chatapp.entities.Room;
import org.chatapp.entities.User;
import org.chatapp.enumerable.MessageType;
import org.chatapp.models.MessageModel;
import org.chatapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;
import java.util.Date;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @Autowired
    private UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        this.userService.makeUserOnline(username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            logger.info(username  + "has Disconnected");
            User user = this.userService.makeUserOffline(username);
            MessageModel message = new MessageModel();
            message.setUser(username);
            message.setMessageType(String.valueOf(MessageType.LEAVE));

            for (Room room : user.getRooms()) {
                messageSendingOperations.convertAndSend("/sendMessage/" + room.getName() , message);
            }

        }
    }
}