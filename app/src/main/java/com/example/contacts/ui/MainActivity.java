package com.example.contacts.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.contacts.db.ContactsDatabase;
import com.example.contacts.R;
import com.example.contacts.db.entity.ContactWithGroups;
import com.example.contacts.db.entity.GroupContacts;
import com.example.contacts.DataRepository;
import com.example.contacts.ui.MainFragment.MainFragment;
import com.example.contacts.viewmodel.ContactViewModel;
import com.example.contacts.viewmodel.factory.Factory;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private ContactViewModel contactViewModel;
    private DataRepository dataRepository;
    private ContactsDatabase contactsDatabase;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbarSubGroup);
        setSupportActionBar(mToolbar);

        fragmentManager = getSupportFragmentManager();
        contactsDatabase = ContactsDatabase.getInstance(getApplicationContext());

        dataRepository = new DataRepository(contactsDatabase);
        contactViewModel = new ViewModelProvider(this, new Factory(dataRepository)).get(ContactViewModel.class);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.containerGroup, new MainFragment())
                    .commit();
        }


//        deleteAll();

    }

    public void insetNewGroup() {
        Thread thread = new Thread(() -> {
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 23"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 24"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 25"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 26"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 27"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 28"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 29"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 30"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 31"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 32"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 33"));
            contactsDatabase.contactsDao().insertNewGroup(new GroupContacts("Группа 34"));

        });
        thread.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void deleteAll() {
        Thread thread = new Thread(() -> {
//            contactsDatabase.contactsDao().deleteAllGroups();
//            contactsDatabase.contactsDao().deleteAllContacts();
//            contactsDatabase.contactsDao().deleteAllSubGroup();
            contactsDatabase.contactsDao().deleteContactWithGroup(2);
        });
        thread.start();

    }


}