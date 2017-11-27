package org.chatapp.serviceImpl;

import org.chatapp.entities.Room;
import org.chatapp.repositories.RoomRepository;
import org.chatapp.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomServiceImpl implements RoomService{

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
}
