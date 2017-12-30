package org.chatapp.repositories;

import org.chatapp.entities.SocialUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialUserRepository  extends UserRepository<SocialUser>{

    SocialUser findOneByEmail(String email);
}
