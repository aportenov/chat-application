package org.chatapp.repositories;

import org.chatapp.entities.AbstractUser;
import org.chatapp.models.UserBindingModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbstractUserRepository extends UserRepository<AbstractUser> {


    @Query(value = "SELECT u FROM AbstractUser AS u LEFT JOIN u.rooms AS r WHERE r.name = :roomName")
    List<AbstractUser> findAllByRoomName(@Param("roomName") String roomName);

    AbstractUser findOneByEmail(String email);
}