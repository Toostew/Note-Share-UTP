package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.service.CustomUserDetailsServiceException;
import com.toostew.noteShare.exception.pojo.service.UserServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try{
            User user = userService.getUserByUsername(username);
            List<GrantedAuthority> grantedAuthorities = user.getAuthoritiesList().stream()
                    .map(authority -> (GrantedAuthority) new SimpleGrantedAuthority(authority.getAuthority()))
                    .toList();

            return new  org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorities);

        } catch (UserServiceException e) {
            throw new CustomUserDetailsServiceException("Issue in Custom UserDetails Service, couldn't find username", e);
        } catch (Exception e) {
            throw new CustomUserDetailsServiceException("Issue in Custom UserDetails Service, Unknown error!", e);
        }
    }


}
