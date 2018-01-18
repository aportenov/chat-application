package org.chatapp.serviceImpl;

import org.chatapp.entities.Room;
import org.chatapp.enumerable.Status;
import org.chatapp.models.UserViewModel;
import org.chatapp.repositories.RoomRepository;
import org.chatapp.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;


    @Override
    public void save(String roomName) {
        Room room = this.roomRepository.findOneByName(roomName);
        if (room == null) {
            this.roomRepository.save(room);
        }
    }

    @Override
    public Room findRoomByName(String roomName) {
        Room room = this.roomRepository.findOneByName(roomName);
        return room;
    }

    @Override
    public String[] findRoomsByUser(String username) {
        List<Room> userUsedRooms = this.roomRepository.findAllByUsersUserName(username);
        List<Room> rooms;
        if (userUsedRooms.isEmpty()) {
            rooms = this.roomRepository.findAll();
        } else {
            rooms = this.roomRepository.findWheneUserIsNot(userUsedRooms);
        }

        String[] userRooms = rooms.stream().map(r -> r.getName()).toArray(String[]::new);
        return userRooms;
    }

    @Override
    public List<UserViewModel> findUsersRoomByName(String currentRoom) {
        Room room = this.roomRepository.findOneByName(currentRoom);
        if (room == null) {
            room = new Room();
        }

        List<UserViewModel> userViewModels = room.getUsers()
                .stream()
                .filter(u -> u.getStatus().equals(Status.ONLINE))
                .map(u -> new UserViewModel(u.getUsername(), u.getImage()))
                .collect(Collectors.toList());

        return userViewModels;
    }

    @Override
    public Room create(String currentRoom) {
        Room room = new Room();
        room.setName(currentRoom);
        return this.roomRepository.save(room);
    }
}
