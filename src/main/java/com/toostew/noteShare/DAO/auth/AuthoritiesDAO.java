package com.toostew.noteShare.DAO.auth;

import com.toostew.noteShare.entity.Authorities;
import com.toostew.noteShare.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class AuthoritiesDAO {

    //this repository no longer implements any interfaces. read note.txt in the DAO folder for more info

    private EntityManager em;

    public AuthoritiesDAO(EntityManager em) {
        this.em = em;
    }


    //Create

    //Read
    public Authorities getAuthoritiesById(int id){
        return em.find(Authorities.class, id);
    }

    //update

    //delete

}