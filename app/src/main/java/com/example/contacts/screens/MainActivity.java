package com.example.contacts.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.contacts.data.ContactsDatabase;
import com.example.contacts.R;
import com.example.contacts.entity.SubGroupContact;
import com.example.contacts.iteractor.usecase.UseCase;
import com.example.contacts.screens.groupFragment.GroupFragment;
import com.example.contacts.screens.viewmodel.ContactViewModel;
import com.example.contacts.screens.viewmodel.factory.ViewModelFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private ContactViewModel viewModel;
    private UseCase useCase;
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

        useCase = new UseCase(contactsDatabase);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(useCase)).get(ContactViewModel.class);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.containerGroup, new GroupFragment())
                    .commit();
        }



//        deleteAll();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

private void deleteAll(){
        Thread thread = new Thread(()->{
            contactsDatabase.contactsDao().deleteAllGroups();
            contactsDatabase.contactsDao().deleteAllContacts();
            contactsDatabase.contactsDao().deleteAllSubGroup();
        });
        thread.start();

}

}