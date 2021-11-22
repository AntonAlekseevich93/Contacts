package com.example.contacts.data.dao;

import android.telecom.Call;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contacts.data.relation.SubGroupOfSelectGroup;
import com.example.contacts.entity.Contact;
import com.example.contacts.entity.ContactWithGroups;
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


//    @Query("SELECT * FROM contacts WHERE mapOfGroup LIKE   '%' || :nameGroup || '%' ")
//    Flowable<List<Contact>> getAllContactsOfGroup(String nameGroup);
//
//    @Query("SELECT * FROM subgroup WHERE `nameGroup` = :nameGroup")
//    Flowable<List<SubGroupContact>> getAllSubGroup(String nameGroup);



    @Query("DELETE FROM groupContacts")
    void deleteAllGroups();

    @Query("DELETE FROM subgroup")
    void deleteAllSubGroup();

    @Query("DELETE FROM contacts")
    void deleteAllContacts();







//




    // От СЮДА НОВЫЕ
    // Добавляет новый контакт в БД (в таблице одно поле - number phone)
    @Insert
    void insertNewContact(Contact contact);

    //Добавляет новый контакт в БД с выбранными группами и подгруппами
    @Query("INSERT INTO contactsWithGroups (idContacts, name, number, description, priority, idGroup, idSubGroup) VALUES (:idContact, :nameContact, :numberContact, :descriptionContact, :priorityContact, :idGroup, :idSubGroup)")
    void insertContactWithGroup(int idContact, String nameContact, String numberContact, String descriptionContact, int priorityContact,
                                int idGroup, int idSubGroup);

    //Добавляет новую группу
    @Insert
    void insertNewGroup(GroupContacts newGroup);

    //Добавляет новую подгруппу
    @Insert
    void insertNewSubGroup(SubGroupContact subGroupContact);

    //Получает все контакты конкретной группы
    @Query("SELECT * FROM contactsWithGroups WHERE idGroup = :idGroup")
    List<ContactWithGroups> getAllContactThisGroup(int idGroup);

    //Получает все контакты конкретной подгруппы
    @Query("SELECT * FROM contactsWithGroups WHERE idSubGroup = :idSubGroup")
    List<ContactWithGroups> getAllContactThisSubGroup(int idSubGroup);

    //Получает id контакта по номеру телефона
    @Query("SELECT `id` FROM contacts WHERE number = :number ")
    int getIdContact(String number);

    //Получает список групп и подгрупп принадлежащих этим группам
    @Query("SELECT id, nameGroup FROM 'groupContacts'" )
    Flowable<List<SubGroupOfSelectGroup>> getAllGroupWithNestedSubGroup();

    // Проверяет, существует ли такая группа
    @Query("SELECT * FROM groupContacts WHERE nameGroup = :nameGroup")
    List<GroupContacts> checkIfGroupExist(String nameGroup);

    //Проверяет, существует ли такой контакт
    @Query("SELECT * FROM contacts WHERE number = :numberContact")
    List<Contact> checkIfContactExist(String numberContact);

    //Проверяет, существует ли такая подгруппа
    @Query("SELECT * FROM subgroup WHERE nameSubGroup = :nameSubGroup")
    List<SubGroupContact> checkIfSubGroupExist(String nameSubGroup);

    @Query("UPDATE groupContacts SET nameGroup = :newName WHERE nameGroup = :name")
    void editNameGroup(String name, String newName);

//    @Query("UPDATE subgroup SET nameGroup = :newNameGroup WHERE nameGroup = :nameGroup")
//    void editNameGroupFromTableSubGroup(String nameGroup, String newNameGroup);
}
