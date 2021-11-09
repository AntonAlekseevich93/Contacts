package com.example.contacts.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "groupContacts")
public class GroupContacts {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameGroup;
    private String subgroup;
    @Ignore
    private boolean isSelect = false;

    public GroupContacts(int id, String nameGroup, String subgroup) {
        this.id = id;
        this.nameGroup = nameGroup;
        this.subgroup = subgroup;
    }

    @Ignore
    public GroupContacts(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    @Ignore
    public GroupContacts(String nameGroup, String subgroup) {
        this.nameGroup = nameGroup;
        this.subgroup = subgroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
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
