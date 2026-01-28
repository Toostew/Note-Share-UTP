package com.toostew.noteShare.DAO.auth;

import com.toostew.noteShare.exception.pojo.DAO.UserDAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import com.toostew.noteShare.entity.User;

@Repository
public class UserDAO {

    //this repository no longer implements any interfaces. read note.txt in the DAO folder for more info

    private EntityManager em;

    public UserDAO(EntityManager em) {
        this.em = em;
    }


    //Create

    //Read
    public User getUserById(int id){
        try{
            return em.find(User.class, id);
        } catch (EntityNotFoundException e) {
            throw new UserDAOException("Issue in UserDAO, User not found!", e);
        }

    }

    //update

    //delete

}
