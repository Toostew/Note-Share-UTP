package com.toostew.noteShare.DAO.auth;

import com.toostew.noteShare.exception.pojo.DAO.UserDAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
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
            throw new UserDAOException("Issue in UserDAO, User ID not found!", e);
        }

    }
    public User getUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :user", User.class)
                    .setParameter("user", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            // em.find returns null if not found,
            // but getSingleResult throws an exception if not found.
            throw new UserDAOException("Issue in UserDAO, Username not found!", e);
        } catch (Exception e) {
            throw new UserDAOException("Issue in UserDAO, Database error", e);
        }
    }


    //update

    //delete

}
