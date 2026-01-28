package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.DAO.auth.UserDAO;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.DAO.UserDAOException;
import com.toostew.noteShare.exception.pojo.service.UserServiceException;
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
        try{
            return userDAO.getUserById(id);
        } catch(UserDAOException e){
            throw new UserServiceException("Issue in User Service,Could not get User by ID", e);
        }

    }

    public User getUserByUsername(String username) {
        try{
            return userDAO.getUserByUsername(username);
        } catch(UserDAOException e){
            throw new UserServiceException("Issue in User Service,Could not get User by Username", e);
        }
    }



}
