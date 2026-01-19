package com.toostew.noteShare.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Owner {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    //we do not include cascades because cascade operations will affect every associated entity
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<File_records> file_recordsList;


    //Id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    //FileRecord
    public List<File_records> getFile_recordsList() {
        return file_recordsList;
    }
    public void setFile_recordsList(List<File_records> file_recordsList) {
        this.file_recordsList = file_recordsList;
    }
}
