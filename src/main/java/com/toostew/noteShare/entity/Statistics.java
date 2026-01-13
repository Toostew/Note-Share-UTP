package com.toostew.noteShare.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Statistics {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="egress_volume")
    private long egress_volume;

    @Column(name="database_transactions")
    private int database_transactions;

    @Column(name="object_transactions")
    private int object_transactions;

    @Column(name="updated_at")
    private LocalDateTime updated_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getEgress_volume() {
        return egress_volume;
    }

    public void setEgress_volume(long egress_volume) {
        this.egress_volume = egress_volume;
    }

    public int getDatabase_transactions() {
        return database_transactions;
    }

    public void setDatabase_transactions(int database_transactions) {
        this.database_transactions = database_transactions;
    }

    public int getObject_transactions() {
        return object_transactions;
    }

    public void setObject_transactions(int object_transactions) {
        this.object_transactions = object_transactions;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
