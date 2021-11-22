package com.example.contacts.iteractor.usecase;

import androidx.annotation.NonNull;

import com.example.contacts.data.ContactsDatabase;
import com.example.contacts.data.relation.SubGroupOfSelectGroup;
import com.example.contacts.entity.Contact;
import com.example.contacts.entity.ContactWithGroups;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;

import org.reactivestreams.Subscriber;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class UseCase {
    private ContactsDatabase contactsDatabase;
    private List<GroupContacts> listGroup;

    public UseCase(ContactsDatabase contactsDatabase) {
        this.contactsDatabase = contactsDatabase;
    }

    // не нужен
//    public Flowable<List<GroupContacts>> getAllGroupOfContacts() {
//        return contactsDatabase.contactsDao().getAllGroupContacts();
//    }

//не нужен
//    public Flowable<List<Contact>> getAllContactsOfGroup(String nameGroup) {
//        System.out.println("Usecase " + nameGroup);
//        return contactsDatabase.contactsDao().getAllContactsOfGroup(nameGroup);
//    }
// не нужен
//    public Flowable<List<SubGroupContact>> getAllSubGroup(String nameSubGroup) {
//        return contactsDatabase.contactsDao().getAllSubGroup(nameSubGroup);
//    }


    // ПЕРЕДЕЛАТЬ


//


    /**
     * НОВЫЕ и ПЕРЕРАБОТАННЫЕ
     */

    /**
     * Метод создает новый контакт и добавляет в БД
     *
     * @param mapGroup        - список выбранных групп (id, name)
     * @param mapOfSubgroup   - список выбранных подгруп (id, name)
     * @param nameContact     - название контакта
     * @param numberContact   - номер контакта
     * @param priorityContact - приоритет
     * @param description     - описание
     * @return true если контакт добавился, false если такой номер уже есть в БД
     */
    public Single<Boolean> createNewContact(Map<Integer, String> mapGroup, Map<Integer, String> mapOfSubgroup, String nameContact,
                                            String numberContact, int priorityContact, String description) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                int size = contactsDatabase.contactsDao().checkIfContactExist(numberContact).size();
                if (size > 0) emitter.onSuccess(false);
                else {
                    contactsDatabase.contactsDao().insertNewContact(new Contact(numberContact));
                    int idNewContact = contactsDatabase.contactsDao().getIdContact(numberContact);
                    for (Integer key : mapGroup.keySet()) {
                        contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                                nameContact, numberContact, description, priorityContact,
                                key, -1);
                    }
                    for (Integer key : mapOfSubgroup.keySet()) {
                        contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                                nameContact, numberContact, description, priorityContact,
                                -1, key);
                    }
                    emitter.onSuccess(true);
                }
            }
        });

    }


    /**
     * Метод получает список групп и принадлежащих этой группе подгрупп
     *
     * @return список групп и подгрупп
     */
    public Flowable<List<SubGroupOfSelectGroup>> getAllGroupWithNestedSubGroup() {

        return contactsDatabase.contactsDao().getAllGroupWithNestedSubGroup();
    }


    /**
     * Метод создает новую группу
     *
     * @param nameGroup - имя группы
     * @return возвращает true если группа добавлена, false если группа с таким названием уже существует
     */
    public Single<Boolean> createNewGroup(String nameGroup) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                int size = contactsDatabase.contactsDao().checkIfGroupExist(nameGroup).size();

                if (size > 0) emitter.onSuccess(false);
                else {
                    contactsDatabase.contactsDao().insertNewGroup(new GroupContacts(nameGroup));
                    emitter.onSuccess(true);
                }


            }
        });
    }


    /**
     * Метод создает новую подгруппу
     *
     * @param idGroup      - id группы
     * @param nameSubGroup - название новой подгруппы
     * @return true если группа создалась, false если такое название подгруппы уже есть
     */
    public Single<Boolean> createNewSubGroup(int idGroup, String nameSubGroup) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                int size = contactsDatabase.contactsDao().checkIfSubGroupExist(nameSubGroup).size();
                if (size > 0) emitter.onSuccess(false);
                else {

                    contactsDatabase.contactsDao().insertNewSubGroup(new SubGroupContact(nameSubGroup, idGroup));

                    emitter.onSuccess(true);
                }
            }
        });
    }

    /**
     * Метод возвращает Список контактов принадлежащих группе или подгруппе
     * @param id - id группы или подгруппы
     * @param type - тип, где 0 - группа, 1 подгруппа
     * @return возвращает List контактов этой группы или подгруппы
     */
    public Flowable<List<ContactWithGroups>> getAllContacts(int id, int type) {
        return new Flowable<List<ContactWithGroups>>() {
            @Override
            protected void subscribeActual(Subscriber<? super List<ContactWithGroups>> s) {
                if (type == 0) s.onNext(contactsDatabase.contactsDao().getAllContactThisGroup(id));
                else if (type == 1)
                    s.onNext(contactsDatabase.contactsDao().getAllContactThisSubGroup(id));
            }
        };
    }

    /**
     * Метод
     * @param name имя группы или подгруппы
     * @param newName новое имя
     * @param type тип, где 0 - группа, 1 - подгруппа
     * @return возвращает true если название изменено, false если такое название уже существует
     */
    public Single<Boolean> editNameGroupOrSubgroup(String name, String newName, int type) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                if(type == 0) {
                    int size = contactsDatabase.contactsDao().checkIfGroupExist(newName).size();
                    if (size > 0) emitter.onSuccess(false);
                    else {
                        contactsDatabase.contactsDao().editNameGroup(name, newName);
                        contactsDatabase.contactsDao().editNameGroup(name, newName);
                        emitter.onSuccess(true);
                    }
                }
            }
        });
    }

}
