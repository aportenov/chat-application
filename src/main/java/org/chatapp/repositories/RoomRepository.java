package org.chatapp.repositories;

import org.chatapp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{

    Room findOneByName(String roomName);

    @Query(value = "SELECT r FROM Room AS r LEFT JOIN r.users AS u WHERE u.username =:username AND u.status = 'ONLINE'")
    List<Room> findAllByUsersUserName(@Param("username") String username);

    @Query(value = "SELECT r FROM Room AS r  WHERE r NOT IN (:userRooms)")
    List<Room> findWheneUserIsNot(@Param("userRooms") List<Room> userRooms);

}
