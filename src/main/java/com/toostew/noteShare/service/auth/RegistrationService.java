package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.entity.Authorities;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.service.UserServiceException;
import com.toostew.noteShare.service.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.Character;

@Service
public class RegistrationService {
    private AuthoritiesService authoritiesService;
    //this Service is for handling new user registration

    private UserService userService;
    private StatisticsService statisticsService;

    public RegistrationService(UserService userService, AuthoritiesService authoritiesService, StatisticsService statisticsService) {
        this.userService = userService;
        this.authoritiesService = authoritiesService;
        this.statisticsService = statisticsService;
    }

    //for security reasons user passwords are never seen naked, always hashed using bcrypt
    public void registerNewUser(String username, String password, String email) {
        //username and password check occurs outside of this method
        //method assumes the username and password are both valid

        //user is saved into database first, then recaptured to be used to create the authorities
        password = bcryptEncodeRawPassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);
        user.setEmail(email);


        System.out.println("Registering New User: " + username + " " + password + "without authorities");
        userService.createUser(user);

        //we persist the user then recapture, because we need user with a generated id to use in creating authorities
        User temp = userService.getUserByUsername(username);

        //create authorities, hardcode USER rank inside
        Authorities authorities = authoritiesService.buildAuthorities("ROLE_USER", temp);
        authoritiesService.createAuthorities(authorities); //persist authorities into the database


    }


    //TODO: there's probably a better way to implement this without using exceptions, fuck it
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

    public boolean emailExists(String email) {
        try{
            User user = userService.getUserByEmail(email);
            //if a user is returned we know that the user/email exists
            return true;
        } catch(UserServiceException ex){
            return false; //if an error occurs we know that the email must not exist
        }
    }

    //encode incoming password into bcrypt with strength 12
    public String bcryptEncodeRawPassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String storedPassword = "{bcrypt}" +  encodedPassword;
        return storedPassword;
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
