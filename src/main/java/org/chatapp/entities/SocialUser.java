package org.chatapp.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "s_user")
public class SocialUser extends AbstractUser{
}
