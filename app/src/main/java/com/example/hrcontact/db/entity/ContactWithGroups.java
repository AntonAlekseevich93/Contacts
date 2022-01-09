package com.example.hrcontact.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contactsWithGroups")
public class ContactWithGroups {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idContacts;
    private String name;
    private String number;
    private String description;
    private int priority;
    private int idGroup;
    private int idSubGroup;
    private int amountSelectedGroup;

    public ContactWithGroups(int id, int idContacts, String name, String number, String description, int priority, int amountSelectedGroup, int idGroup, int idSubGroup) {
        this.id = id;
        this.idContacts = idContacts;
        this.name = name;
        this.number = number;
        this.description = description;
        this.priority = priority;
        this.amountSelectedGroup = amountSelectedGroup;
        this.idGroup = idGroup;
        this.idSubGroup = idSubGroup;
    }

    @Ignore
    public ContactWithGroups(int idContacts, String name, String number, String description, int priority, int idGroup, int idSubGroup, int amountSelectedGroup) {
        this.idContacts = idContacts;
        this.name = name;
        this.number = number;
        this.description = description;
        this.priority = priority;
        this.idGroup = idGroup;
        this.idSubGroup = idSubGroup;
        this.amountSelectedGroup = amountSelectedGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdContacts() {
        return idContacts;
    }

    public void setIdContacts(int idContacts) {
        this.idContacts = idContacts;
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

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public int getIdSubGroup() {
        return idSubGroup;
    }

    public void setIdSubGroup(int idSubGroup) {
        this.idSubGroup = idSubGroup;
    }

    public int getAmountSelectedGroup() {
        return amountSelectedGroup;
    }

    public void setAmountSelectedGroup(int amountSelectedGroup) {
        this.amountSelectedGroup = amountSelectedGroup;
    }
}
