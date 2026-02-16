package com.toostew.noteShare.entity.jointable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class File_records_tagsEmbeddedKey implements Serializable {

    @Column(name = "file_records_id")
    private int file_records_id;

    @Column(name = "tags_id")
    private int tags_id;

    public File_records_tagsEmbeddedKey() {}

    public File_records_tagsEmbeddedKey(int file_records_id, int tags_id) {
        this.file_records_id = file_records_id;
        this.tags_id = tags_id;
    }

    public int getFile_records_id() {
        return file_records_id;
    }
    public void setFile_records_id(int file_records_id) {
        this.file_records_id = file_records_id;
    }

    public int getTags_id() {
        return tags_id;
    }
    public void setTags_id(int tags_id) {
        this.tags_id = tags_id;
    }

    //Serializable
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        File_records_tagsEmbeddedKey that = (File_records_tagsEmbeddedKey) obj;
        return that.getTags_id() == tags_id && file_records_id == that.file_records_id;

    }

    @Override
    public int hashCode() {
        return Objects.hash(file_records_id, tags_id);
    }
}
