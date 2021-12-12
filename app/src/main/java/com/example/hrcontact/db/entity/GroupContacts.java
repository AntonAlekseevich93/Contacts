package com.example.hrcontact.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "groupContacts")
public class GroupContacts {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameGroup;
    @Ignore
    private boolean isSelect = false;
    @Ignore
    private boolean isExpandable = false;
    @Ignore
    private int counterSelectedSubGroup = 0;


    public GroupContacts(int id, String nameGroup) {
        this.id = id;
        this.nameGroup = nameGroup;
    }

    @Ignore
    public GroupContacts(String nameGroup) {
        this.nameGroup = nameGroup;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Ignore
    public boolean isExpandable() {
        return isExpandable;
    }

    @Ignore
    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    @Ignore
    public int getCounterSelectedSubGroup() {
        return counterSelectedSubGroup;
    }

    @Ignore
    public void setCounterSelectedSubGroup(boolean b) {
        if (b) this.counterSelectedSubGroup++;
        else this.counterSelectedSubGroup--;
    }

    @Ignore
    public void setCounterSelectedSubGroup(int counterSelectedSubGroup) {
        this.counterSelectedSubGroup = counterSelectedSubGroup;
    }
}
