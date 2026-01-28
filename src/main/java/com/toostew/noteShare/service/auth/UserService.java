package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.DAO.auth.UserDAO;
import com.toostew.noteShare.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    //service layer for accessing User

    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }



}
