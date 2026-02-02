package com.toostew.noteShare.service;

import com.toostew.noteShare.DAO.FileDAOInterface;
import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.exception.pojo.service.FileServiceException;
import com.toostew.noteShare.exception.pojo.DAO.File_recordsDAOException;
import com.toostew.noteShare.exception.pojo.service.StatisticsServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    //this service is a layer for CRUD operations for File_records entities
    //DONE: naming convention: rename the files on upload to follow a convention before uploading to object storage and mysql
    //idea: uuid-datecreated

    private FileDAOInterface dao;
    private StatisticsService statisticsService;



    public FileService(FileDAOInterface dao, StatisticsService statisticsService) {
        this.dao = dao;
        this.statisticsService = statisticsService;
    }

    //generate a UUID for storing files following naming convention:
    //UUID-DATE-CREATED
    public String createNewFileRecordStoredName(){
        UUID uuid = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        return uuid.toString()+"-"+now.toString();
    }

    //receive file_records list, output list where viewable == 1
    //this is a separate service since there are already so many implementation for listing file_records
    //reimplementing this would take a long ass time
    //TODO: reimplement so as to not make this a 2 step process by default, abstract it
    public List<File_records> filterOnlyViewableRecords(List<File_records> file_recordsList){
        var resultList = new ArrayList<File_records>();
        for(File_records file_records : file_recordsList){
            if(file_records.getViewable()){
                resultList.add(file_records); //add the file_records if it is viewable
            }
        }

        return resultList;
    }

    //filter only by non-viewable
    public List<File_records> filterOnlyNonViewableRecords(List<File_records> file_recordsList){
        var resultList = new ArrayList<File_records>();
        for(File_records file_records : file_recordsList){
            if(!file_records.getViewable()){
                resultList.add(file_records); //add the file_records strictly if not viewable
            }
        }
        return resultList;
    }

    //create
    public void createFile_record(File_records file_records){
        try{
            dao.createFile_records(file_records);
            statisticsService.incrementDatabaseTransactions(1);
        } catch(File_recordsDAOException e){
            throw new FileServiceException("Issue at FileService, issue with creating File_record",e);
        } catch(StatisticsServiceException e){
            throw new FileServiceException("Issue at FileService, issue with creating File_record",e);
        }

    }

    //read
    public File_records getFile_recordById(int id){
        try{
            statisticsService.incrementDatabaseTransactions(1);
            return dao.getFile_recordById(id);
        } catch(File_recordsDAOException e){
            throw new FileServiceException("Issue at FileService, issue with fetching File_record by id",e);
        } catch(StatisticsServiceException e){
            throw new FileServiceException("Issue at FileService, issue with fetching File_record by id",e);
        }

    }

    //read a certain number of entries. If -1, return all
    public List<File_records> getNumFile_Records(int num){
        try{
            statisticsService.incrementDatabaseTransactions(num);
            return dao.getFile_records(num);
        } catch(File_recordsDAOException e){
            throw new FileServiceException("Issue at FileService, issue with fetching File_record by id",e);
        } catch(StatisticsServiceException e){
            throw new FileServiceException("Issue at FileService, issue with fetching File_record by id",e);
        }
    }

    //update
    public void updateFile_record(File_records file_records){
        try{
            dao.updateFile_records(file_records);
        } catch (File_recordsDAOException e){
            throw new FileServiceException("Issue at FileService, Could not update file_records",e);
        }
    }

    //delete
    public void deleteFile_record(int id){
        try{
            dao.deleteFile_recordById(id);
            statisticsService.incrementDatabaseTransactions(1);
        } catch(File_recordsDAOException e){
            throw new FileServiceException("Issue at FileService, issue with deleting File_record by id",e);
        } catch(StatisticsServiceException e){
            throw new FileServiceException("Issue at FileService, issue with deleting File_record by id",e);
        }
    }




}
