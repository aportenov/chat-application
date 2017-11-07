package org.chatapp.serviceImpl;

import org.chatapp.entities.User;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Override
    public void save(User user) {
    }
}
