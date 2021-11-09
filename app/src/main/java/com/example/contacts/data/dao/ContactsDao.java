package com.example.contacts.data.dao;

import android.telecom.Call;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contacts.entity.Contact;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ContactsDao {
//    @Insert
//    void createGroup(GroupContacts groupContacts);

    @Query("SELECT * FROM groupContacts ORDER BY nameGroup ASC")
    Flowable<List<GroupContacts>> getAllGroupContacts();

    @Query("SELECT * FROM contacts WHERE `group` = :nameGroup")
    Flowable<List<Contact>> getAllContactsOfGroup(String nameGroup);

    @Query("SELECT * FROM subgroup WHERE `nameGroup` = :nameGroup")
    Flowable<List<SubGroupContact>> getAllSubGroup(String nameGroup);



    @Query("DELETE FROM groupContacts")
    void deleteAllGroups();

    @Query("DELETE FROM subgroup")
    void deleteAllSubGroup();

    @Query("DELETE FROM contacts")
    void deleteAllContacts();

    @Query("SELECT * FROM groupContacts WHERE nameGroup = :nameGroup")
    List<GroupContacts> checkIfGroupExist(String nameGroup);

    @Query("SELECT * FROM contacts WHERE number = :numberContact")
    List<Contact> checkIfContactExist(String numberContact);

    @Query("SELECT * FROM subgroup WHERE nameSubGroup = :nameSubGroup")
    List<SubGroupContact> checkIfSubGroupExist(String nameSubGroup);

    @Query("UPDATE groupContacts SET nameGroup = :newNameGroup WHERE nameGroup = :nameGroup")
    void editNameGroup(String nameGroup, String newNameGroup);

    @Query("UPDATE subgroup SET nameGroup = :newNameGroup WHERE nameGroup = :nameGroup")
    void editNameGroupFromTableSubGroup(String nameGroup, String newNameGroup);


    @Insert
    void insertNewGroup(GroupContacts newGroup);

    @Insert
    void insertNewContact(Contact contact);

    @Insert
    void insertNewSubGroup(SubGroupContact subGroupContact);

}
