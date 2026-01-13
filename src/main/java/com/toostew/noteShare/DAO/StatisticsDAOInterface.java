package com.toostew.noteShare.DAO;

import com.toostew.noteShare.entity.Statistics;

public interface StatisticsDAOInterface {

    //CRUD
    public void createStatistics(Statistics statistics);

    public Statistics getStatistics();

    public void updateStatistics(Statistics statistics);

    public void deleteStatistics(Statistics statistics);

}
