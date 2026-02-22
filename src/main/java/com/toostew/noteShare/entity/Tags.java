package com.toostew.noteShare.entity;

import com.toostew.noteShare.entity.jointable.File_records_tags;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "tag_name")
    private String tag_name;

    @OneToMany(mappedBy = "tags")
    private List<File_records_tags> file_records_tags;

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

    public List<File_records_tags> getFile_records_tags() {
        return file_records_tags;
    }

    public void setFile_records_tags(List<File_records_tags> file_records_tags) {
        this.file_records_tags = file_records_tags;
    }
}
