package org.chatapp.serviceImpl;

import org.chatapp.entities.AbstractUser;
import org.chatapp.entities.Role;
import org.chatapp.entities.Room;
import org.chatapp.entities.User;
import org.chatapp.enumerable.Status;
import org.chatapp.messages.Errors;
import org.chatapp.models.UserBindingModel;
import org.chatapp.repositories.RoleRepository;
import org.chatapp.repositories.UserRepository;
import org.chatapp.services.RoomService;
import org.chatapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final RoomService roomService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           ModelMapper modelMapper,
                           RoomService roomService,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.roomService = roomService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void save(UserBindingModel userBindingModel) {
        AbstractUser user = new User();
        user.setUsername(userBindingModel.getUsername());
        user.setEmail(userBindingModel.getEmail());
        String encryptedPassword = this.bCryptPasswordEncoder.encode(userBindingModel.getPassword());
        user.setImage(userBindingModel.getImage());
        user.setPassword(encryptedPassword);
        user.setStatus(Status.OFFLINE);
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
        AbstractUser user = this.userRepository.findOneByUsername(username);
        String[] userRooms = user.getRooms().stream().map(r -> r.getName()).toArray(String[]::new);

        return userRooms;
    }

    @Override
    public AbstractUser findUserByName(String username) {
        AbstractUser user = this.userRepository.findOneByUsername(username);
        return user;
    }

    @Override
    public void addRoom(String username, String room) {
        AbstractUser user = this.userRepository.findOneByUsername(username);
        Room currentRoom = this.roomService.findRoomByName(room);
        if (currentRoom  == null) {
            currentRoom = this.roomService.create(room);
        }

        if (!user.getRooms().contains(currentRoom)){
            user.addRoom(currentRoom);
        }

        this.userRepository.save(user);
    }

    @Override
    public void leaveRoom(String username, String room) {
        AbstractUser user = this.userRepository.findOneByUsername(username);
        Room roomToLeft = this.roomService.findRoomByName(room);
        if (user.getRooms().contains(roomToLeft)) {
            user.removeRoom(roomToLeft);
        }
    }

    @Override
    public AbstractUser changeUserStatus(String username) {
        AbstractUser user = this.userRepository.findOneByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(Errors.INVALID_USER);
        }

        if (user.getStatus() == Status.ONLINE) {
            user.setStatus(Status.OFFLINE);
        } else {
            user.setStatus(Status.ONLINE);
        }

        this.userRepository.save(user);
        return user;
    }


    @Override
    public Boolean findUserByEmail(String email) {
        AbstractUser user = null;
        if (email != null) {
            user  =  this.userRepository.findOneByEmail(email);

        }

        return user == null ? true : false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AbstractUser user = this.userRepository.findOneByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException(Errors.INVALID_USER);
        }

         return user;
    }
}
