package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.StatisticsDAOInterface;
import com.toostew.noteShare.entity.Statistics;
import com.toostew.noteShare.exception.pojo.DAO.StatisticsDAOException;
import com.toostew.noteShare.exception.pojo.service.StatisticsServiceException;
import org.springframework.stereotype.Service;


//this class handles all basic services for Statistics
@Service
public class StatisticsService {

    private StatisticsDAOInterface dao;


    public StatisticsService(StatisticsDAOInterface dao) {
        this.dao = dao;
    }


    //get Statistics
    public Statistics getStatistics(){
        try{
            return dao.getStatistics();
        } catch (StatisticsDAOException e) {
            throw new StatisticsServiceException("Issue in Statistics service, could not get statistics!",e);
        }

    }


    //get egress volume
    public long getEgressVolume(){
        try{
            Statistics temp = dao.getStatistics();
            return temp.getEgress_volume();
        } catch(StatisticsDAOException e){
            throw new StatisticsServiceException("Issue in Statistics service, could not get statistics!",e);
        }
    }

    public int getDatabaseTransactions(){
        try{
            Statistics temp = dao.getStatistics();
            return temp.getDatabase_transactions();
        } catch(StatisticsDAOException e){
            throw new StatisticsServiceException("Issue in Statistics service, could not get statistics!",e);
        }
    }

    public int getObjectTrasactions(){
        try{
            Statistics temp = dao.getStatistics();
            return temp.getObject_transactions();
        } catch(StatisticsDAOException e){
            throw new StatisticsServiceException("Issue in Statistics service, could not get statistics!",e);
        }

    }


    //increment egress volume
    public void incrementEgressVolume(long size){
        try{
            Statistics temp = dao.getStatistics();
            temp.setEgress_volume(temp.getEgress_volume() + size);
            dao.updateStatistics(temp);
            System.out.println("Incremented Egress Volume by "+size+" bytes");
        } catch(StatisticsDAOException e){
            throw new StatisticsServiceException("Issue in Statistics service, could not increment egress volume!",e);
        }

    }

    //increment database transactions
    public void incrementDatabaseTrasactions(){
        try{
            Statistics temp = dao.getStatistics();
            temp.setDatabase_transactions(temp.getDatabase_transactions() + 1);
            dao.updateStatistics(temp);
            System.out.println("Incremented Database Transactions");
        } catch(StatisticsDAOException e){
            throw new StatisticsServiceException("Issue in Statistics service, could not increment database transactions!",e);
        }

    }

    //increment object transactions
    public void incrementObjectTrasactions(){
        try{
            Statistics temp = dao.getStatistics();
            temp.setObject_transactions(temp.getObject_transactions() + 1);
            dao.updateStatistics(temp);
            System.out.println("Incremented Object Transactions");
        } catch(StatisticsDAOException e){
            throw new StatisticsServiceException("Issue in Statistics service, could not increment object transactions!",e);
        }

    }



}
