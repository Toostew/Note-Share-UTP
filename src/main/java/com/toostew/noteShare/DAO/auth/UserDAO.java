package com.toostew.noteShare.DAO.auth;

import jakarta.persistence.EntityManager;
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
        return em.find(User.class, id);
    }

    //update

    //delete

}
