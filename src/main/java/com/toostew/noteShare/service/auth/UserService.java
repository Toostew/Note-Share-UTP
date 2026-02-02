package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.DAO.auth.UserDAO;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.DAO.UserDAOException;
import com.toostew.noteShare.exception.pojo.service.UserServiceException;
import com.toostew.noteShare.service.StatisticsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    //service layer for accessing User

    private UserDAO userDAO;
    private StatisticsService statisticsService;

    public UserService(UserDAO userDAO, StatisticsService statisticsService) {

        this.userDAO = userDAO;
        this.statisticsService = statisticsService;
    }



    //create
    public void createUser(User user) throws UserServiceException {
        userDAO.createUser(user);
        statisticsService.incrementDatabaseTransactions(1);
    }


    public User getUserById(int id) {
        try{
            statisticsService.incrementDatabaseTransactions(1);
            return userDAO.getUserById(id);
        } catch(UserDAOException e){
            throw new UserServiceException("Issue in User Service,Could not get User by ID", e);
        }

    }

    public User getUserByUsername(String username) {
        try{
            statisticsService.incrementDatabaseTransactions(1);
            return userDAO.getUserByUsername(username);
        } catch(UserDAOException e){
            throw new UserServiceException("Issue in User Service,Could not get User by Username", e);
        }
    }

    public User getUserByEmail(String email) {
        try{
            statisticsService.incrementDatabaseTransactions(1);
            return userDAO.getUserByEmail(email);
        } catch(UserDAOException e){
            throw new UserServiceException("Issue in User Service,Could not get User by Email", e);
        }

    }



}
