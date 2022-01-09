package com.example.hrcontact;

import android.content.Context;

import com.example.hrcontact.db.ContactsDatabase;
import com.example.hrcontact.db.dao.ContactsDao;
import com.example.hrcontact.support.BottomSheetDialogGroup;
import com.example.hrcontact.support.IClickListenerDismiss;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@dagger.Module
@InstallIn(SingletonComponent.class)
public  class AppModule {


    @Singleton
    @Provides
    public static ContactsDatabase contactsDatabase(@ApplicationContext Context context) {
        return ContactsDatabase.getInstance(context);
    }

    @Provides
    public static ContactsDao contactsDao(ContactsDatabase contactsDatabase) {
        return contactsDatabase.contactsDao();
    }




}
