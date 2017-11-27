package org.chatapp.services;

import org.chatapp.entities.User;
import org.chatapp.models.UserBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService  {

    void save(UserBindingModel username);

    String[] findRoomsByUser(String username);

    User findUserByName(String username);
}
