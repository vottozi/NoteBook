package com.example.studentschedule.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")

public class Student {
    @PrimaryKey(autoGenerate = true)
    Long id;

    @ColumnInfo(name = "name_surname")
    private String name_surname;
    @ColumnInfo(name = "number")
    private String tel_number;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Student(Long id, String name_surname, String tel_number, byte[] image) {
        this.id = id;
        this.name_surname = name_surname;
        this.tel_number = tel_number;
        this.image = image;
    }

    @Ignore
    public Student(String name_surname, String tel_number, byte[] image) {
        this.name_surname = name_surname;
        this.tel_number = tel_number;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName_surname() {
        return name_surname;
    }

    public void setName_surname(String name_surname) {
        this.name_surname = name_surname;
    }

    public String getTel_number() {
        return tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
