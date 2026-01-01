package com.toostew.noteShare.entity;


import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class File_records {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "original_name")
    private String original_name;

    @Column(name = "stored_name")
    private String stored_name;

    @Column(name = "content_type")
    private String content_type;

    @Column(name = "size")
    private long size;

    @Column(name = "storage_path")
    private String storage_path; //this is the bucket name

    @Column(name = "owner_id")
    private int owner_id; //TODO: implement sign ins for user identification/authorization

    @Column(name = "date_created")
    private LocalDate date_created;

    public File_records() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getStored_name() {
        return stored_name;
    }

    public void setStored_name(String stored_name) {
        this.stored_name = stored_name;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStorage_path() {
        return storage_path;
    }

    public void setStorage_path(String storage_path) {
        this.storage_path = storage_path;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    @Override
    public String toString() {
        return "FILE: " + "\n"
                + "id: " + id + "\n"
                + "original_name: " + original_name + "\n"
                + "stored_name: " + stored_name + "\n"
                + "content_type: " + content_type + "\n"
                + "size: " + size + "\n"
                + "storage_path: " + storage_path + "\n"
                + "owner_id: " + owner_id + "\n"
                + "date_created: " + date_created + "\n";
    }
}
