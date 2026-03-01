package com.toostew.noteShare.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "tag_name")
    private String tag_name;

    @ManyToMany(mappedBy = "tags")
    private List<File_records> file_records;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public List<File_records> getFile_records() {
        return file_records;
    }

    public void setFile_records_tags(List<File_records> file_records) {
        this.file_records = file_records;
    }
}
