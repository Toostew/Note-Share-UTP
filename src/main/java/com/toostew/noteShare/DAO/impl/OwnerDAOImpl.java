package com.toostew.noteShare.DAO.impl;

import com.toostew.noteShare.DAO.OwnerDAOInterface;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.Owner;
import com.toostew.noteShare.exception.pojo.other.OwnerDAOException;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OwnerDAOImpl implements OwnerDAOInterface {

    private EntityManager em;

    public OwnerDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void createOwner(Owner owner) {
        try{
            em.persist(owner);
        } catch(EntityExistsException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Entity already exists!", e);
        } catch(IllegalArgumentException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Illegal Argument!", e);
        }
    }

    @Override
    public Owner getOwner(int id) {
        try{
            return em.find(Owner.class, id);
        } catch (EntityNotFoundException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Entity Not Found!", e);
        } catch (IllegalArgumentException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Illegal Argument!", e);
        }
    }

    @Override
    public List<Owner> getAllOwners() {
        try{
            TypedQuery<Owner> query = em.createQuery("from Owner f", Owner.class);
            return  query.getResultList();
        } catch (EntityNotFoundException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Entity Not Found!", e);
        } catch (IllegalArgumentException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Illegal Argument!", e);
        } catch (Exception e){
            //placeholder catcher
            throw new OwnerDAOException("Issue in OwnerDAO, unexpected issue!", e);
        }

    }

    @Override
    public void updateOwner(Owner owner) {
        try{
            Owner temp =  em.find(Owner.class, owner.getId());
            temp.setName(owner.getName());
            em.merge(temp);
        } catch(EntityNotFoundException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Entity Not Found!", e);
        } catch (IllegalArgumentException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Illegal Argument!", e);
        } catch (Exception e){
            throw new OwnerDAOException("Issue in OwnerDAO, unexpected issue!", e);
        }

    }

    @Override
    public void deleteOwner(int id) {
        try{
            Owner temp = em.find(Owner.class, id);
            em.remove(temp);
        } catch(EntityNotFoundException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Entity Not Found!", e);
        } catch (IllegalArgumentException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Illegal Argument!", e);
        } catch (Exception e){
            throw new OwnerDAOException("Issue in OwnerDAO, unexpected issue!", e);
        }
    }


    @Override
    public List<File_records> getFileRecordsList(int ownerId) {
        try{
            Owner owner = em.find(Owner.class, ownerId);
            return owner.getFile_recordsList();
        } catch(EntityNotFoundException e){
            throw new OwnerDAOException("Issue in OwnerDAO, Entity Not Found!", e);
        }

    }
}
