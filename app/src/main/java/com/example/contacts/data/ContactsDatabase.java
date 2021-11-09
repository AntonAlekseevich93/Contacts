package com.example.contacts.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.contacts.data.dao.ContactsDao;
import com.example.contacts.entity.Contact;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;

@Database(entities = {GroupContacts.class, Contact.class, SubGroupContact.class}, version = 4, exportSchema = false)
public abstract class ContactsDatabase extends RoomDatabase {
    private static ContactsDatabase contactsDatabase;
    private static final String DB_NAME = "contact.db";
    private static final Object LOCK = new Object();

    public static ContactsDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (contactsDatabase == null) {
                contactsDatabase = Room.databaseBuilder(context, ContactsDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return contactsDatabase;
    }

    public abstract ContactsDao contactsDao();
}
