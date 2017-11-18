package org.chatapp.services;

import org.chatapp.models.UserBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService  {

    void save(UserBindingModel user);
}
