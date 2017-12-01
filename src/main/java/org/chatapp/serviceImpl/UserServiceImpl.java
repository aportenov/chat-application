package org.chatapp.serviceImpl;

import org.chatapp.entities.Role;
import org.chatapp.entities.Room;
import org.chatapp.entities.User;
import org.chatapp.enumerable.Status;
import org.chatapp.models.UserBindingModel;
import org.chatapp.repositories.RoleRepository;
import org.chatapp.repositories.UserRepository;
import org.chatapp.services.RoomService;
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
        User user = new User();
        user.setUsername(userBindingModel.getUsername());
        user.setFullName(userBindingModel.getFullName());
        String encryptedPassword = this.bCryptPasswordEncoder.encode(userBindingModel.getPassword());
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
    public void addRoom(String username, String room) {
        User user = this.userRepository.findByUsername(username);
        Room currentRoom = this.roomService.findRoomByName(room);
        if (!user.getRooms().contains(currentRoom)){
            user.addRoom(currentRoom);
        }
    }

    @Override
    public void leaveRoom(String username, String room) {
        User user = this.userRepository.findByUsername(username);
        Room roomToLeft = this.roomService.findRoomByName(room);
        if (user.getRooms().contains(roomToLeft)) {
            user.removeRoom(roomToLeft);
        }
    }

    @Override
    public void makeUserOnline(String username) {
        User user = this.userRepository.findByUsername(username);
//        if(user == null){
//            throw new UsernameNotFoundException(UserNotFound);
//        }

        user.setStatus(Status.ONLINE);
        this.userRepository.save(user);
    }

    @Override
    public User makeUserOffline(String username) {
        User user = this.userRepository.findByUsername(username);
//        if(user == null){
//            throw new UsernameNotFoundException(UserNotFound);
//        }

        user.setStatus(Status.OFFLINE);
        this.userRepository.save(user);

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
