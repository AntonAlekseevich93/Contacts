package com.example.contacts.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String number;
    private String group;
    private String subgroup;
    private String description;
    private int priority;


    public Contact(int id, String name, String number, String group, String subgroup, String description, int priority) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.group = group;
        this.subgroup = subgroup;
        this.description = description;
        this.priority = priority;
    }

    @Ignore
    public Contact(String name, String number, String group, String subgroup, String description, int priority) {
        this.name = name;
        this.number = number;
        this.group = group;
        this.subgroup = subgroup;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
