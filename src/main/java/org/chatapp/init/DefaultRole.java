package org.chatapp.init;

import org.chatapp.entities.Role;
import org.chatapp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DefaultRole {

    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    private void addDefaultRole(){
        long count = this.roleRepository.count();

        if (count == 0){
            Role role = new Role();
            role.setAuthority(ROLE_USER);
            this.roleRepository.save(role);
        }
    }
}
