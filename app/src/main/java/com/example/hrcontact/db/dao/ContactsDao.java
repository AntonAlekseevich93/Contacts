package com.example.hrcontact.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hrcontact.db.entity.Contact;
import com.example.hrcontact.db.entity.ContactWithGroups;
import com.example.hrcontact.db.entity.GroupContacts;
import com.example.hrcontact.db.entity.SubGroupContact;
import com.example.hrcontact.model.SubGroupOfSelectGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;

@Dao
public interface ContactsDao {
    // Добавляет новый контакт в БД (в таблице одно поле - number phone)
    @Insert
    void insertNewContact(Contact contact);

    @Query("UPDATE contacts set number =:number WHERE id = :idContact")
    void updateContact(int idContact, String number);

    //Добавляет новый контакт в БД с выбранными группами и подгруппами
    @Query("INSERT INTO contactsWithGroups (idContacts, name, number, description, priority, amountSelectedGroup, idGroup, idSubGroup)" +
            " VALUES (:idContact, :nameContact, :numberContact, :descriptionContact, :priorityContact," +
            ":amountSelectedGroup, :idGroup, :idSubGroup)")
    void insertContactWithGroup(int idContact, String nameContact, String numberContact, String descriptionContact, int priorityContact, int amountSelectedGroup, int idGroup, int idSubGroup);

    //Добавляет новую группу
    @Insert
    void insertNewGroup(GroupContacts newGroup);

    //Добавляет новую подгруппу
    @Insert
    void insertNewSubGroup(SubGroupContact subGroupContact);

    //Удаляет контакт из таблицы (контакты с группами)
    @Query("DELETE FROM contactsWithGroups WHERE idContacts =:idContact")
    void deleteContactWithGroup(int idContact);

    //Удаляет контакт из таблицы контактов
    @Query("DELETE FROM contacts WHERE id =:idContact")
    void deleteContact(int idContact);

    //Удаляет группу из таблицы групп
    @Query("DELETE FROM groupContacts WHERE id =:idGroup")
    void deleteGroup(int idGroup);

    //Удаляет группу из таблицы контактов
    @Query("UPDATE contactsWithGroups SET idGroup = :defaultValue WHERE idGroup = :id")
    void deleteGroupFromContact(int id, int defaultValue);

    //Удаляет все подгруппы которые относятся к текущей группу
    @Query("DELETE FROM subgroup WHERE idGroup =:idGroup")
    void deleteSubgroupWhereGroupIsExist(int idGroup);

    //Удаляет подгруппу из таблицы подгрупп
    @Query("DELETE FROM subgroup WHERE id =:idSubGroup")
    void deleteSubGroup(int idSubGroup);

    //Удаляет подгруппу из таблицы контактов
    @Query("UPDATE contactsWithGroups SET idSubGroup = :defaultValue WHERE idSubGroup = :id")
    void deleteSubGroupFromContact(int id, int defaultValue);

    //Удаляет копии контактов без групп и подгрупп
    @Query("DELETE FROM contactsWithGroups WHERE idGroup = -1 AND idSubGroup = -1")
    void deleteCopyContacts();

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
    @Query("SELECT id, nameGroup FROM 'groupContacts'")
    Flowable<List<SubGroupOfSelectGroup>> getAllGroupWithNestedSubGroup();

    @Query("SELECT * FROM contactsWithGroups WHERE idContacts = :idContact")
    List<ContactWithGroups> getContact(int idContact);

    // Проверяет, существует ли такая группа
    @Query("SELECT * FROM groupContacts WHERE nameGroup = :nameGroup")
    List<GroupContacts> checkIfGroupExist(String nameGroup);

    //Проверяет, существует ли такая подгруппа
    @Query("SELECT * FROM subgroup WHERE nameSubGroup = :nameSubGroup")
    List<SubGroupContact> checkIfSubGroupExist(String nameSubGroup);

    //Проверяет, существует ли такой контакт
    @Query("SELECT * FROM contacts WHERE number = :numberContact")
    List<Contact> checkIfContactExist(String numberContact);

    //Проверяет, существует ли уже номер телефона измененного контакта
    @Query("SELECT * FROM contacts WHERE number = :numberContact AND NOT (id = :idContact)")
    List<Contact> checkIfEditableContactExist(String numberContact, int idContact);

    //Изменяет название группы
    @Query("UPDATE groupContacts SET nameGroup = :newName WHERE nameGroup = :name")
    void editNameGroup(String name, String newName);

    //Изменяет название подгруппы
    @Query("UPDATE subgroup SET nameSubGroup = :newName WHERE nameSubGroup = :name")
    void editNameSubGroup(String name, String newName);

    // Получает все группы для выбранного контакта
    @Query("SELECT * FROM groupContacts WHERE id = :idGroup")
    GroupContacts getGroupForContact(int idGroup);

    //Получает все подгруппы для выбранного контакта
    @Query("SELECT * FROM subgroup WHERE id = :idSubGroup")
    SubGroupContact getSubGroupForContact(int idSubGroup);

    @Query("SELECT * FROM contactsWithGroups WHERE idGroup =:idGroup ")
    List<ContactWithGroups> getContactWithThisGroup(int idGroup);

    @Query("UPDATE contactsWithGroups SET amountSelectedGroup = :amount WHERE idContacts =:idContact")
    void updateAmountSelectedGroupForContact(int amount, int idContact);
}
