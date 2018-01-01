package org.chatapp.serviceImpl;

import org.chatapp.entities.SocialUser;
import org.springframework.social.facebook.api.User;
import org.chatapp.repositories.RoleRepository;
import org.chatapp.repositories.SocialUserRepository;
import org.chatapp.services.SocialUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SocialUserServiceImpl implements SocialUserService {

    private final SocialUserRepository socialUserRepository;
    private final RoleRepository roleRepository;

    public SocialUserServiceImpl(SocialUserRepository socialUserRepository, RoleRepository roleRepository) {
        this.socialUserRepository = socialUserRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    private SocialUser registerFbUser(User facebookUser, String image, String email) {
        SocialUser user = new SocialUser();
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setUsername(facebookUser.getName());
        user.setEmail(email);
        user.setImage(image);
        user.addRole(this.roleRepository.findOne(1L));
        this.socialUserRepository.save(user);

        return user;
    }

    @Override
    public void proceedFbUser(User facebookUser, String image) {
        String email = facebookUser.getEmail();
        SocialUser socialUser = this.socialUserRepository.findOneByEmail(email);
        if (socialUser == null) {
            socialUser = registerFbUser(facebookUser, image, email);
        }

        loginUser(socialUser);
    }

    private void loginUser(SocialUser socialUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(socialUser, null, socialUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}