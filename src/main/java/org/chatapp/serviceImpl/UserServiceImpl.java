package org.chatapp.serviceImpl;

import org.chatapp.entities.Role;
import org.chatapp.entities.User;
import org.chatapp.models.UserBindingModel;
import org.chatapp.repositories.RoleRepository;
import org.chatapp.repositories.UserRepository;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void save(UserBindingModel userBindingModel) {
        User user = new User();
        user.setUsername(userBindingModel.getUsername());
        user.setFullName(userBindingModel.getFullName());
        String encryptedPassword = this.bCryptPasswordEncoder.encode(userBindingModel.getPassword());
        user.setPassword(encryptedPassword);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        Role currRole = this.roleRepository.findOne(1L);
        user.getRoles().add(currRole);
        this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
//        if(user == null){
//            throw new UsernameNotFoundException(Errors.INVALID_CREDENTIALS);
//        }

         return user;
    }
}
