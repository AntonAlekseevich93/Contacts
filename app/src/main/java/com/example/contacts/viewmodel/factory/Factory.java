package com.example.contacts.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.contacts.DataRepository;
import com.example.contacts.viewmodel.ContactViewModel;

public class Factory extends ViewModelProvider.NewInstanceFactory {
    private final DataRepository dataRepository;

    public Factory(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
    }

    @NonNull
    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ContactViewModel.class) {
            return (T) new ContactViewModel(dataRepository);
        }
        return null;
    }

}
