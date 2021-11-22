package com.example.contacts.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subgroup")
public class SubGroupContact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameSubGroup;
    private int idGroup;
    @Ignore
    private boolean isSelect = false;


    public SubGroupContact(int id, String nameSubGroup, int idGroup) {
        this.id = id;
        this.nameSubGroup = nameSubGroup;
        this.idGroup = idGroup;
    }

    @Ignore
    public SubGroupContact(String nameSubGroup, int idGroup) {
        this.nameSubGroup = nameSubGroup;
        this.idGroup = idGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSubGroup() {
        return nameSubGroup;
    }

    public void setNameSubGroup(String nameSubGroup) {
        this.nameSubGroup = nameSubGroup;
    }


    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    @Ignore
    public boolean isSelect() {
        return isSelect;
    }

    @Ignore
    public void setSelect(boolean select) {
        isSelect = select;
    }
}
