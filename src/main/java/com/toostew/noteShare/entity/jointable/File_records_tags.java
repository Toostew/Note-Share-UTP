package com.toostew.noteShare.entity.jointable;

import com.toostew.noteShare.entity.File_records;
import com.toostew.noteShare.entity.Tags;
import jakarta.persistence.*;

@Entity
public class File_records_tags {

    @EmbeddedId
    File_records_tagsEmbeddedKey id = new File_records_tagsEmbeddedKey();

    @ManyToOne
    @MapsId("file_records_id") //for @EmbeddedId, selects which of the fields in the Composite key to refer to
    @JoinColumn(name = "file_records_id") //name of the actual attribute in the table that holds FK
    File_records file_records;

    @ManyToOne
    @MapsId("tags_id") //for @EmbeddedId, selects which of the fields in the Composite key to refer to
    @JoinColumn(name = "tags_id") //name of the actual attribute in the table that holds FK
    Tags tags;

    public File_records_tagsEmbeddedKey getId() {
        return id;
    }

    //no setter for id

    public File_records getFile_records() {
        return file_records;
    }

    public void setFile_records(File_records file_records) {
        this.file_records = file_records;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }
}
