package com.toostew.noteShare.entity;

import jakarta.persistence.*;

@Entity
public class Thumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "stored_name")
    private String stored_name;

    @ManyToOne
    @JoinColumn(name = "file_records_id") //name of the actual FK in the table that points to file_records
    private File_records file_records;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStored_name() {
        return stored_name;
    }

    public void setStored_name(String stored_name) {
        this.stored_name = stored_name;
    }

    public File_records getFile_records() {
        return file_records;
    }

    public void setFile_records(File_records file_records) {
        this.file_records = file_records;
    }
}
