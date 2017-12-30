package org.chatapp.entities;

import org.chatapp.enumerable.MessageType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String name;

    @Column(name = "password", nullable = true)
    private String password;

    @ManyToMany(mappedBy = "rooms")
    private Set<AbstractUser> users;

    public Room() {
        this.users = new LinkedHashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<AbstractUser> getUsers() {
        return users;
    }

    public void addUsers(User user) {
        this.getUsers().add(user);
    }
}
