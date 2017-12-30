package org.chatapp.services;


import org.springframework.social.facebook.api.User;

public interface SocialUserService {

    void proceedFbUser(User facebookUser, String image);
}

