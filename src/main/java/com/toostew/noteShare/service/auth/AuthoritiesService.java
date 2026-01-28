package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.DAO.auth.AuthoritiesDAO;
import com.toostew.noteShare.entity.Authorities;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {

    //authorities service layer

    private AuthoritiesDAO authoritiesDAO;

    public AuthoritiesService(AuthoritiesDAO authoritiesDAO) {
        this.authoritiesDAO = authoritiesDAO;
    }

    public Authorities getAuthoritiesById(int id) {
        return authoritiesDAO.getAuthoritiesById(id);
    }
}
