package org.chatapp.serviceImpl;

import org.chatapp.entities.Room;
import org.chatapp.repositories.RoomRepository;
import org.chatapp.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

@Service
@Transactional
public class RoomServiceImpl implements RoomService{

    @Autowired
    private RoomRepository roomRepository;


    @Override
    public void save(Room room) {
        this.roomRepository.save(room);
    }
}
