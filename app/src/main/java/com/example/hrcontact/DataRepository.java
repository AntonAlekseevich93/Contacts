package com.example.hrcontact;

import com.example.hrcontact.db.ContactsDatabase;
import com.example.hrcontact.db.entity.Contact;
import com.example.hrcontact.db.entity.ContactWithGroups;
import com.example.hrcontact.db.entity.GroupContacts;
import com.example.hrcontact.db.entity.SubGroupContact;
import com.example.hrcontact.model.SubGroupOfSelectGroup;
import com.example.hrcontact.support.ActionEnum;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class DataRepository {
    private final ContactsDatabase contactsDatabase;
    private List<Map<Integer, String>> maps = new ArrayList<>();


    public DataRepository(ContactsDatabase contactsDatabase) {
        this.contactsDatabase = contactsDatabase;
    }

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
    public Single<Boolean> createNewContact(Map<Integer, String> mapGroup, Map<Integer,
            String> mapOfSubgroup, String nameContact, String numberContact,
                                            int priorityContact, String description) {
        return Single.create(emitter -> {
            int size = contactsDatabase.contactsDao().checkIfContactExist(numberContact).size();
            if (size > 0) emitter.onSuccess(false);
            else {

                contactsDatabase.contactsDao().insertNewContact(new Contact(numberContact));
                int idNewContact = contactsDatabase.contactsDao().getIdContact(numberContact);
                if (mapGroup != null && mapGroup.size() > 0) {
                    int amountSelectedGroup = mapGroup.size();
                    for (Integer key : mapGroup.keySet()) {
                        contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                                nameContact, numberContact, description, priorityContact,
                                amountSelectedGroup, key, -1);
                    }
                    for (Integer key : mapOfSubgroup.keySet()) {
                        contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                                nameContact, numberContact, description, priorityContact,
                                amountSelectedGroup, -1, key);
                    }
                } else {
                    //Случай без группы (устанавливаем группу "Без групп")
                    contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                            nameContact, numberContact, description, priorityContact, 0,
                            1, -1);
                }
                emitter.onSuccess(true);
            }
        });

    }

    public Single<Boolean> saveEditedContact(Map<Integer, String> mapOfGroup, Map<Integer,
            String> mapOfSubGroup, int idEditableContact, String nameContact, String numberContact,
                                             int priorityContact, String description) {
        return Single.create(emitter -> {
            int size = contactsDatabase.contactsDao()
                    .checkIfEditableContactExist(numberContact, idEditableContact).size();
            if (size > 0) emitter.onSuccess(false);
            else {
                contactsDatabase.contactsDao().updateContact(idEditableContact, numberContact);
                int idNewContact = contactsDatabase.contactsDao().getIdContact(numberContact);
                contactsDatabase.contactsDao().deleteContactWithGroup(idNewContact);


                if (mapOfGroup != null && mapOfGroup.size() > 0 && !mapOfGroup.containsKey(1)) {
                    int amountSelectedGroup = mapOfGroup.size();
                    for (Integer key : mapOfGroup.keySet()) {
                        if (key == 1) continue;//Если выбрана без группы
                        contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                                nameContact, numberContact, description, priorityContact,
                                amountSelectedGroup, key, -1);
                    }
                    for (Integer key : mapOfSubGroup.keySet()) {
                        contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                                nameContact, numberContact, description, priorityContact,
                                amountSelectedGroup, -1, key);
                    }
                } else {
                    // Случай если без группы
                    contactsDatabase.contactsDao().insertContactWithGroup(idNewContact,
                            nameContact, numberContact, description, priorityContact, 0,
                            1, -1);
                }
                emitter.onSuccess(true);
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
        return Single.create(emitter -> {
            int size = contactsDatabase.contactsDao().checkIfGroupExist(nameGroup).size();
            if (size > 0) emitter.onSuccess(false);
            else {
                contactsDatabase.contactsDao().insertNewGroup(new GroupContacts(nameGroup));
                emitter.onSuccess(true);
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
        return Single.create(emitter -> {
            int size = contactsDatabase.contactsDao().checkIfSubGroupExist(nameSubGroup).size();
            if (size > 0) emitter.onSuccess(false);
            else {
                contactsDatabase.contactsDao()
                        .insertNewSubGroup(new SubGroupContact(nameSubGroup, idGroup));
                emitter.onSuccess(true);
            }
        });
    }

    /**
     * Метод возвращает Список контактов принадлежащих группе или подгруппе
     *
     * @param id   - id группы или подгруппы
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
     * Метод изменяет название подгруппы
     *
     * @param name    имя группы или подгруппы
     * @param newName новое имя
     * @param type    тип, где 0 - группа, 1 - подгруппа
     * @return возвращает true если название изменено, false если такое название уже существует
     */
    public Single<Boolean> editNameGroupOrSubgroup(String name, String newName, int type) {
        return Single.create(emitter -> {
            if (type == 0) {
                int size = contactsDatabase.contactsDao().checkIfGroupExist(newName).size();
                if (size > 0) emitter.onSuccess(false);
                else {
                    contactsDatabase.contactsDao().editNameGroup(name, newName);
                    contactsDatabase.contactsDao().editNameGroup(name, newName);
                    emitter.onSuccess(true);
                }
            } else if (type == 1) {
                int size = contactsDatabase.contactsDao().checkIfSubGroupExist(newName).size();
                if (size > 0) emitter.onSuccess(false);
                else {
                    contactsDatabase.contactsDao().editNameSubGroup(name, newName);
                    contactsDatabase.contactsDao().editNameSubGroup(name, newName);
                    emitter.onSuccess(true);
                }
            }
        });
    }

    /**
     * Метод получает 1) Contact для редактирования.
     * Из контакта создает две Map (1-группы для этого контакта, подгруппы для этого контакта)
     */
    public Flowable<ContactWithGroups> getSelectedContactForEdit(int idContact) {
        return new Flowable<ContactWithGroups>() {
            @Override
            protected void subscribeActual(Subscriber<? super ContactWithGroups> s) {
                List<ContactWithGroups> list = contactsDatabase.contactsDao().getContact(idContact);
                ContactWithGroups contact = list.get(0);
                s.onNext(contact);

                Map<Integer, String> mapOfGroup = new HashMap<>();
                Map<Integer, String> mapOfSubGroup = new HashMap<>();
                for (ContactWithGroups c : list) {
                    if (c.getIdGroup() > 0) {
                        if (!mapOfGroup.containsKey(c.getIdGroup())) {
                            GroupContacts g = contactsDatabase.contactsDao()
                                    .getGroupForContact(c.getIdGroup());
                            mapOfGroup.put(g.getId(), g.getNameGroup());
                        }
                    }
                    if (c.getIdSubGroup() > 0) {
                        if (!mapOfSubGroup.containsKey(c.getIdGroup())) {
                            SubGroupContact sb = contactsDatabase.contactsDao()
                                    .getSubGroupForContact(c.getIdSubGroup());
                            mapOfSubGroup.put(sb.getId(), sb.getNameSubGroup());
                        }
                    }
                }
                maps.add(mapOfGroup);
                maps.add(mapOfSubGroup);
            }
        };
    }

    /**
     * @return возвращает список Map с группами и подгруппами принадлежащими к конкретному контакту
     * Map ранее получены в методе getSelectedContactForEdit(int idContact)
     */
    public Flowable<List<Map<Integer, String>>> getMapGroupAndSubgroupThisContact() {
        return new Flowable<List<Map<Integer, String>>>() {
            @Override
            protected void subscribeActual(Subscriber<? super List<Map<Integer, String>>> s) {
                s.onNext(maps);
            }
        };
    }


    /**
     * Метод удаляет контакт, группу или подгруппу
     *
     * @param actionEnum удаление контакта или группы или подгруппы
     * @param idDelete   id контакта, группы или подгруппы
     */
    public void delete(ActionEnum actionEnum, int idDelete) {
        Thread thread = new Thread(() -> {
            switch (actionEnum) {
                case DELETE_CONTACT:
                    contactsDatabase.contactsDao().deleteContactWithGroup(idDelete);
                    contactsDatabase.contactsDao().deleteContact(idDelete);
                    break;

                case DELETE_GROUP:
                    List<ContactWithGroups> contacts = contactsDatabase.contactsDao().getContactWithThisGroup(idDelete);


                    contactsDatabase.contactsDao().deleteGroup(idDelete);
                    contactsDatabase.contactsDao().deleteGroupFromContact(idDelete, -1);
                    contactsDatabase.contactsDao().deleteSubgroupWhereGroupIsExist(idDelete);

                    //Если эта группа последняя у контакта, тогда нам нужно установить для контакта
                    //группу - Без группы
                    for (ContactWithGroups contact : contacts) {
                        int amountSelectedGroup = contact.getAmountSelectedGroup();
                        int idContact = contact.getIdContacts();
                        if (amountSelectedGroup == 1) {
                            String nameContact = contact.getName();
                            String numberContact = contact.getNumber();
                            String description = contact.getDescription();
                            int priorityContact = contact.getPriority();
                            contactsDatabase.contactsDao().deleteContactWithGroup(idContact);
                            contactsDatabase.contactsDao().insertContactWithGroup(idContact,
                                    nameContact, numberContact, description, priorityContact, 0,
                                    1, -1);
                        } else {
                            amountSelectedGroup--;
                            contactsDatabase.contactsDao()
                                    .updateAmountSelectedGroupForContact(amountSelectedGroup, idContact);
                        }
                    }

                    break;

                case DELETE_SUBGROUP:
                    contactsDatabase.contactsDao().deleteSubGroup(idDelete);
                    contactsDatabase.contactsDao().deleteSubGroupFromContact(idDelete, -1);
                    contactsDatabase.contactsDao().deleteCopyContacts();
                    break;
            }
        });
        thread.start();
    }
}
