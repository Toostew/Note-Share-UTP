package com.toostew.noteShare.DAO.auth;

import com.toostew.noteShare.entity.Authorities;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.DAO.AuthoritiesDAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class AuthoritiesDAO {

    //this repository no longer implements any interfaces. read note.txt in the DAO folder for more info

    private EntityManager em;

    public AuthoritiesDAO(EntityManager em) {
        this.em = em;
    }


    //Create
    @Transactional
    public void createAuthorities(Authorities authorities) throws AuthoritiesDAOException {
        System.out.println("Adding new authority of id: "+authorities.getId());
        em.persist(authorities);
    }


    //Read
    public Authorities getAuthoritiesById(int id){
        try{
            return em.find(Authorities.class, id);
        } catch (EntityNotFoundException e) {
            throw new AuthoritiesDAOException("Issue in AuthoritiesDAO, Authorities not found!", e);
        }

    }

    //update
    //can currently only modify authority string
    public void updateAuthorities(Authorities authorities){
        try{
            System.out.println("updating authorities for authority ID: "+authorities.getId());
            Authorities temp = em.find(Authorities.class, authorities.getId());
            temp.setAuthority(authorities.getAuthority());
        } catch(EntityNotFoundException e){
            throw new  AuthoritiesDAOException("Issue in AuthoritiesDAO, Authorities not found!", e);
        } catch(Exception e){
            throw new AuthoritiesDAOException("Issue in AuthoritiesDAO, Unknown error!", e);
        }
    }

    //delete
    //delete with provided authority
    public void deleteAuthorities(Authorities authorities){
        try{
            System.out.println("Deleteing authorities for authority ID: "+authorities.getId());
            Authorities temp = em.find(Authorities.class, authorities.getId());
            em.remove(temp);
        } catch(EntityNotFoundException e){
            throw new AuthoritiesDAOException("Issue in AuthoritiesDAO, Authorities not found!", e);
        } catch(Exception e){
            throw new AuthoritiesDAOException("Issue in AuthoritiesDAO, Unknown error!", e);
        }
    }
}