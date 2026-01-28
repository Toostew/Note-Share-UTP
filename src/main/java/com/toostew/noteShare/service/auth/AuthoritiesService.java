package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.DAO.auth.AuthoritiesDAO;
import com.toostew.noteShare.entity.Authorities;
import com.toostew.noteShare.exception.pojo.DAO.AuthoritiesDAOException;
import com.toostew.noteShare.exception.pojo.service.AuthoritiesServiceException;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {

    //authorities service layer

    private AuthoritiesDAO authoritiesDAO;

    public AuthoritiesService(AuthoritiesDAO authoritiesDAO) {
        this.authoritiesDAO = authoritiesDAO;
    }

    public Authorities getAuthoritiesById(int id) {
        try{
            return authoritiesDAO.getAuthoritiesById(id);
        } catch (AuthoritiesDAOException e) {
            throw new AuthoritiesServiceException("Issue in Authorities Service, Could not get Authorities by ID", e);
        }

    }
}
