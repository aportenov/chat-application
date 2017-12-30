package org.chatapp.serviceImpl;

import org.chatapp.entities.AbstractUser;
import org.chatapp.entities.Message;
import org.chatapp.entities.Room;
import org.chatapp.entities.User;
import org.chatapp.enumerable.MessageType;
import org.chatapp.models.MessageModel;
import org.chatapp.repositories.MessageRepository;
import org.chatapp.services.MessageService;
import org.chatapp.services.RoomService;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;
    private final RoomService roomService;
    private final UserService userService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              RoomService roomService,
                              UserService userService) {
        this.messageRepository = messageRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    @Override
    public void addMessage(MessageModel message) {
        Room room = this.roomService.findRoomByName(message.getRoomName());
        AbstractUser user = this.userService.findUserByName(message.getUser());
        Message chatMessage = new Message();
        chatMessage.setMessage(message.getMessage());
        chatMessage.setDate(Date.from(Instant.now()));
        chatMessage.setUser(user);
        chatMessage.setRoom(room);
        chatMessage.setMessageType(MessageType.valueOf(message.getMessageType()));
        this.messageRepository.save(chatMessage);
    }
}
