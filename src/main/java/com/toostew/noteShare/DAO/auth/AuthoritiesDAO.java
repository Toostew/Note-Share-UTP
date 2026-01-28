package com.toostew.noteShare.DAO.auth;

import com.toostew.noteShare.entity.Authorities;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.DAO.AuthoritiesDAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
        try{
            return em.find(Authorities.class, id);
        } catch (EntityNotFoundException e) {
            throw new AuthoritiesDAOException("Issue in AuthoritiesDAO, Authorities not found!", e);
        }

    }

    //update

    //delete

}