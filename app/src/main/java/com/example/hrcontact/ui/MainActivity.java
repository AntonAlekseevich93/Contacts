package com.example.hrcontact.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.contacts.R;
import com.example.hrcontact.DataRepository;
import com.example.hrcontact.db.ContactsDatabase;
import com.example.hrcontact.ui.MainFragment.MainFragment;
import com.example.hrcontact.viewmodel.ContactViewModel;
import com.example.hrcontact.viewmodel.factory.Factory;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbarSubGroup);

        //отключаем темную тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setSupportActionBar(mToolbar);



        FragmentManager fragmentManager = getSupportFragmentManager();
        ContactsDatabase contactsDatabase = ContactsDatabase.getInstance(getApplicationContext());

        DataRepository dataRepository = new DataRepository(contactsDatabase);
        ContactViewModel contactViewModel =
                new ViewModelProvider(this, new Factory(dataRepository)).get(ContactViewModel.class);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.containerGroup, new MainFragment())
                    .commit();
        }

    }



}