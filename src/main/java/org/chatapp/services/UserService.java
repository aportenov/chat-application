package org.chatapp.services;

import org.chatapp.entities.AbstractUser;
import org.chatapp.models.UserBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService  {

    void save(UserBindingModel username);

    String[] findRoomsByUser(String username);

    AbstractUser findUserByName(String username);

    void addRoom(String username, String room);

    void leaveRoom(String username, String room);

    void makeUserOnline(String username);

    AbstractUser makeUserOffline(String username);
}
