package com.example.contacts.screens.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.contacts.iteractor.usecase.UseCase;
import com.example.contacts.screens.viewmodel.ContactViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final UseCase useCase;

    public ViewModelFactory(UseCase useCase) {
        super();
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ContactViewModel.class) {
            return (T) new ContactViewModel(useCase);
        }
        return null;
    }
}
