package com.toostew.noteShare.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="category")
    private String category;


    //we need to set it to eager fetching so it fetches everything
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<File_records> file_recordsList = new ArrayList<>();

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

    //Category
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    //File_records
    public List<File_records> getFile_recordsList() {
        return file_recordsList;
    }
    public void setFile_recordsList(List<File_records> file_recordsList) {
        this.file_recordsList = file_recordsList;
    }
}
