package com.toostew.noteShare.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "date_created")
    private LocalDate date_created;

    @Column(name = "viewable")
    private boolean viewable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "file_records_tags",
            joinColumns = @JoinColumn(name = "file_records_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id"))
    List<Tags> tags;

    @OneToMany(mappedBy = "file_records") //the name of the field within the entity that has the FK(many to one) to this entity
    Set<Thumbnail> thumbnail;


    //limited to 25 characters
    @Column(name = "verified")
    private String verified;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    public boolean getViewable() {
        return viewable;
    }

    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }

    public Course getCourse() {return course;}

    public void setCourse(Course course) {this.course = course;}

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public Set<Thumbnail> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Set<Thumbnail> thumbnail) {
        this.thumbnail = thumbnail;
    }

    //special method to return only one Thumbnail
    public Thumbnail getSingleThumbnail() {
        Set<Thumbnail> thumbnails = getThumbnail();
        Thumbnail temp = new Thumbnail();
        for(Thumbnail thumbnail : thumbnails){
            temp = thumbnail;
        }
        return temp;
    }

    public boolean hasThumbnail(){
        Set<Thumbnail> thumbnails = getThumbnail();
        if(thumbnails.isEmpty()){
            return false;
        }
        return true;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
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
                + "user_id: " + user.getId() + "\n"
                + "date_created: " + date_created + "\n"
                + "course_id: " + course.getId() + "\n";
    }
    
}
