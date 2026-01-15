package com.toostew.noteShare.DAO;




import com.toostew.noteShare.entity.Owner;

import java.util.List;

public interface OwnerDAOInterface {


    void createOwner(Owner owner);

    Owner getOwner(int id);

    List<Owner> getAllOwners();

    void updateOwner(Owner owner);

    void deleteOwner(int id);

}
