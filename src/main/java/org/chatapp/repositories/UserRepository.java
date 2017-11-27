package org.chatapp.repositories;

import org.chatapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT u FROM User AS u LEFT JOIN u.rooms AS r WHERE r.name = :roomName")
    List<User> findAllByRoomName(@Param("roomName") String roomName);
}
