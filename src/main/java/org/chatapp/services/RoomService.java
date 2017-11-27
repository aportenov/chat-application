package org.chatapp.services;

import org.chatapp.entities.Room;

public interface RoomService {

    void save(String roomName);

    Room findRoomByName(String roomName);
}
