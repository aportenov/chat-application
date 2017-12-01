package org.chatapp.services;

import org.chatapp.entities.Room;
import org.chatapp.models.UserViewModel;

import java.util.List;

public interface RoomService {

    void save(String roomName);

    Room findRoomByName(String roomName);

    String[] findRoomsByUser(String username);

    List<UserViewModel> findUsersRoomByName(String currentRoom);
}
