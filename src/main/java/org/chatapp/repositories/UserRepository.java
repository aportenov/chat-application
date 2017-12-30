package org.chatapp.repositories;

import org.chatapp.entities.AbstractUser;
import org.chatapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@NoRepositoryBean
public interface UserRepository<T extends AbstractUser> extends JpaRepository<T , Long>{

    T findOneByUsername(String user);

}

