package com.example.contacts.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subgroup")
public class SubGroupContact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameSubGroup;
    private String nameGroup;



    @Ignore
    private boolean isSelect = false;

    @Ignore
    public boolean isSelect() {
        return isSelect;
    }

    @Ignore
    public void setSelect(boolean select) {
        isSelect = select;
    }

    public SubGroupContact(int id, String nameSubGroup, String nameGroup) {
        this.id = id;
        this.nameSubGroup = nameSubGroup;
        this.nameGroup = nameGroup;
    }

    @Ignore
    public SubGroupContact(String nameSubGroup, String nameGroup) {
        this.nameSubGroup = nameSubGroup;
        this.nameGroup = nameGroup;
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

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }
}
