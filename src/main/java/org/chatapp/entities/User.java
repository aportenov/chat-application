package org.chatapp.entities;


import javax.persistence.*;


@Entity
@DiscriminatorValue(value = "user")
public class User extends AbstractUser {

}
