package org.chatapp.serviceImpl;

import org.chatapp.entities.User;
import org.chatapp.models.UserBindingModel;
import org.chatapp.repositories.UserRepository;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(UserBindingModel userBindingModel) {
        User user = new User();
        user.setUsername(userBindingModel.getUsername());
        user.setFullName(userBindingModel.getFullName());
        user.setPassword(userBindingModel.getPassword());
        this.userRepository.save(user);
    }
}
