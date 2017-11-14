package org.chatapp.serviceImpl;

import org.chatapp.entities.User;
import org.chatapp.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Override
    public void save(User user) {
    }
}
