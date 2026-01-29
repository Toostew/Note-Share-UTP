package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.entity.Authorities;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.service.UserServiceException;
import org.springframework.stereotype.Service;

import java.lang.Character;

@Service
public class RegistrationService {
    private final AuthoritiesService authoritiesService;
    //this Service is for handling new user registration

    private UserService userService;

    public RegistrationService(UserService userService, AuthoritiesService authoritiesService) {
        this.userService = userService;
        this.authoritiesService = authoritiesService;
    }


    public void registerNewUser(String username, String password){
        //username and password check occurs outside of this method
        //method assumes the username and password are both valid

        //user is saved into database first, then recaptured to be used to create the authorities
        password = "{noop}"  + password; //spring security requirement, needs either noop or bcrypt
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); //TODO: passwords are currently unencrypted, add bcrypt!
        user.setEnabled(true);


        System.out.println("Registering New User: " + username + " " + password + "without authorities");
        userService.createUser(user);

        //we persist the user then recapture, because we need user with a generated id to use in creating authorities
        User temp = userService.getUserByUsername(username);

        //create authorities, hardcode USER rank inside
        Authorities authorities = authoritiesService.buildAuthorities("USER", temp);
        authoritiesService.createAuthorities(authorities); //persist authorities into the database
    }


    //TODO: there's probably a better way to implement this without using exceptions
    public boolean usernameExists(String username){
        try{
            User user = userService.getUserByUsername(username);
        } catch(UserServiceException ex){
            //if an error occurs, we know the username must not exist
            return false;
        }
        //otherwise, the username already exists
        return true;

    }
    public boolean validPassword(String password){
        //entered password should follow convention
        int capitalLetters = 0;
        int smallLetters = 0;
        int numbers = 0;
        for(int i = 0; i < password.length(); i++){
            Character  c = password.charAt(i);
            if(Character.isDigit(c)){
                numbers++;
            } else if (Character.isUpperCase(c)){
                capitalLetters++;
            } else if (Character.isLowerCase(c)){
                smallLetters++;
            }
        }
        if(capitalLetters >= 1 && smallLetters >= 1 && numbers >= 1){
            //atleast 1 of each to be valid
            return true;
        } else  {
            return false;
        }
    }
}
