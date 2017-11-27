package org.chatapp.serviceImpl;

import org.chatapp.entities.Role;
import org.chatapp.entities.User;
import org.chatapp.models.UserBindingModel;
import org.chatapp.repositories.RoleRepository;
import org.chatapp.repositories.UserRepository;
import org.chatapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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
    public String[] findRoomsByUser(String username) {
        User user = userRepository.findByUsername(username);
        String[] userRooms = user.getRooms().stream().map(r -> r.getName()).toArray(String[]::new);

        return userRooms;
    }

    @Override
    public User findUserByName(String username) {
        User user = this.userRepository.findByUsername(username);
        return user;
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
