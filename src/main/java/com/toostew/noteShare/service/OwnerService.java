package com.toostew.noteShare.service;


import com.toostew.noteShare.DAO.OwnerDAOInterface;
import com.toostew.noteShare.entity.Owner;
import com.toostew.noteShare.exception.pojo.other.OwnerDAOException;
import com.toostew.noteShare.exception.pojo.other.OwnerServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {
    //service for accessing owners

    private OwnerDAOInterface dao;

    public OwnerService(OwnerDAOInterface dao){
        this.dao = dao;
    }


    public void createOwner(Owner owner){
        try{
            dao.createOwner(owner);
        } catch(OwnerDAOException e){
            throw new OwnerServiceException("Issue in Course Service, could not create owner",e);
        }
    }

    public Owner getOwner(int id){
        try{
            return dao.getOwner(id);
        } catch(OwnerDAOException e){
            throw new OwnerServiceException("Issue in Course Service, could not get owner",e);
        }
    }

    public List<Owner> getAllOwners(){
        try{
            return dao.getAllOwners();
        } catch(OwnerDAOException e){
            throw new OwnerServiceException("Issue in Course Service, could not get all owners",e);
        }
    }

    public void updateOwner(Owner owner){
        try{
            dao.updateOwner(owner);
        } catch(OwnerDAOException e){
            throw new OwnerServiceException("Issue in Course Service, could not update owner",e);
        }
    }

    public void deleteOwner(int id){
        try{
            dao.deleteOwner(id);
        } catch(OwnerDAOException e){
            throw new OwnerServiceException("Issue in Course Service, could not delete owner",e);
        }
    }

}
