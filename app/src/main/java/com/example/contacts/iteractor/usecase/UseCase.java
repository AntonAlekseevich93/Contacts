package com.example.contacts.iteractor.usecase;

import androidx.annotation.NonNull;

import com.example.contacts.data.ContactsDatabase;
import com.example.contacts.entity.Contact;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;

import java.util.GregorianCalendar;
import java.util.List;

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


    public Flowable<List<GroupContacts>> getAllGroupOfContacts() {
        return contactsDatabase.contactsDao().getAllGroupContacts();
    }

    public Flowable<List<Contact>> getAllContactsOfGroup(String nameGroup) {
        System.out.println("Usecase " + nameGroup);
        return contactsDatabase.contactsDao().getAllContactsOfGroup(nameGroup);
    }

    public Flowable<List<SubGroupContact>> getAllSubGroup(String nameSubGroup) {
        return contactsDatabase.contactsDao().getAllSubGroup(nameSubGroup);
    }

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

    public Single<Boolean> createNewContact(String nameGroup, String nameSubGroup, String nameContact,
                                            String numberContact, int priorityContact, String description) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                int size = contactsDatabase.contactsDao().checkIfContactExist(numberContact).size();
                if (size > 0) emitter.onSuccess(false);
                else {
                    contactsDatabase.contactsDao().insertNewContact(new Contact(nameContact,
                            numberContact, nameGroup, nameSubGroup, description, priorityContact));
                    emitter.onSuccess(true);
                }
            }
        });

    }

    public Single<Boolean> createNewSubGroup(String nameGroup, String nameSubGroup) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                int size = contactsDatabase.contactsDao().checkIfSubGroupExist(nameSubGroup).size();
                if (size > 0) emitter.onSuccess(false);
                else {
                    contactsDatabase.contactsDao().insertNewSubGroup(new SubGroupContact(nameSubGroup, nameGroup));

                    emitter.onSuccess(true);
                }
            }
        });
    }


    public Single<Boolean> editNameGroup(String nameSelectedGroup, String newNameGroup) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                int size = contactsDatabase.contactsDao().checkIfGroupExist(newNameGroup).size();
                if (size > 0) emitter.onSuccess(false);
                else {
                    contactsDatabase.contactsDao().editNameGroup(nameSelectedGroup, newNameGroup);
                    contactsDatabase.contactsDao().editNameGroupFromTableSubGroup(nameSelectedGroup, newNameGroup);
                    emitter.onSuccess(true);
                }
            }
        });
    }
}
