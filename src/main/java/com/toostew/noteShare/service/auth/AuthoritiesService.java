package com.toostew.noteShare.service.auth;

import com.toostew.noteShare.DAO.auth.AuthoritiesDAO;
import com.toostew.noteShare.entity.Authorities;
import com.toostew.noteShare.entity.User;
import com.toostew.noteShare.exception.pojo.DAO.AuthoritiesDAOException;
import com.toostew.noteShare.exception.pojo.service.AuthoritiesServiceException;
import com.toostew.noteShare.service.StatisticsService;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {

    //authorities service layer

    private AuthoritiesDAO authoritiesDAO;
    private StatisticsService statisticsService;

    public AuthoritiesService(AuthoritiesDAO authoritiesDAO, StatisticsService statisticsService) {
        this.authoritiesDAO = authoritiesDAO;
        this.statisticsService = statisticsService;
    }



    public Authorities buildAuthorities(String role, User user) throws AuthoritiesServiceException {
        Authorities authorities = new Authorities();
        authorities.setAuthority(role);
        authorities.setUser(user);

        return authorities;
    }

    public void createAuthorities(Authorities authorities) throws AuthoritiesDAOException {
        authoritiesDAO.createAuthorities(authorities);
        statisticsService.incrementDatabaseTransactions(1);
    }

    public Authorities getAuthoritiesById(int id) {
        try{
            statisticsService.incrementDatabaseTransactions(1);
            return authoritiesDAO.getAuthoritiesById(id);
        } catch (AuthoritiesDAOException e) {
            throw new AuthoritiesServiceException("Issue in Authorities Service, Could not get Authorities by ID", e);
        }
    }

    //update, delete implementation for the service layer is not done, but it can be done since it's available
    //on the DAO layer
}
