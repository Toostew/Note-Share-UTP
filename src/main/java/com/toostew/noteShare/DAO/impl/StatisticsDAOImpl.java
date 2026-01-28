package com.toostew.noteShare.DAO.impl;

import com.toostew.noteShare.DAO.StatisticsDAOInterface;
import com.toostew.noteShare.entity.Statistics;
import com.toostew.noteShare.exception.pojo.DAO.StatisticsDAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsDAOImpl implements StatisticsDAOInterface {

    private EntityManager entityManager;

    public StatisticsDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional
    @Override
    public void createStatistics(Statistics statistics) {
        //leave empty, we will not create new entries, just alter an existing one
    }

    @Override
    public Statistics getStatistics() {
        try{
            return entityManager.find(Statistics.class,1);
        } catch (IllegalArgumentException e) {
            throw new StatisticsDAOException("Issue in StatisticsDAO, Argument error!",e);
        }

    }
    @Transactional
    @Override
    public void updateStatistics(Statistics statistics) {
        try{
            Statistics temp = entityManager.find(Statistics.class,1);
            temp.setEgress_volume(statistics.getEgress_volume());
            temp.setDatabase_transactions(statistics.getDatabase_transactions());
            temp.setObject_transactions(statistics.getObject_transactions());
            entityManager.merge(temp);
        } catch (IllegalArgumentException e) {
            throw new StatisticsDAOException("Issue in StatisticsDAO, Argument error!",e);
        } catch (TransactionRequiredException e) {
            throw new StatisticsDAOException("Issue in StatisticsDAO, Transaction required!",e);
            //realistically, this error wont pop up since it is marked with transactional
        }

    }
    @Transactional
    @Override
    public void deleteStatistics(Statistics statistics) {
        //leave empty, not implemented
    }
}
