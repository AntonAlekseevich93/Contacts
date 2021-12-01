package com.example.hrcontact.model;

import androidx.room.Relation;

import com.example.hrcontact.db.entity.SubGroupContact;

import java.util.List;

public class SubGroupOfSelectGroup {
    private int id;
    private String nameGroup;
    @Relation(parentColumn = "id", entityColumn = "idGroup")
    private List<SubGroupContact> listSubGroup;

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

    public List<SubGroupContact> getListSubGroup() {
        return listSubGroup;
    }

    public void setListSubGroup(List<SubGroupContact> listSubGroup) {
        this.listSubGroup = listSubGroup;
    }
}
