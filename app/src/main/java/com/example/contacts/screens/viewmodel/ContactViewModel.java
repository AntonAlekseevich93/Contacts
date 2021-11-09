package com.example.contacts.screens.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.contacts.entity.Contact;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;
import com.example.contacts.iteractor.usecase.UseCase;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactViewModel extends ViewModel {
    private MutableLiveData<List<GroupContacts>> liveDataGroupContacts;
    private MutableLiveData<List<Contact>> liveDataContacts;
    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<List<SubGroupContact>> liveDataSubGroupContact;
    private UseCase useCase;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String sSelectGroup = "";
    private String sSelectSubGroup = "";


    public ContactViewModel(UseCase useCase) {
        this.useCase = useCase;
    }

    //Получаем все группы при первичной загрузке фрагмента
    public LiveData<List<GroupContacts>> getAllGroupContacts() {
        if (liveDataGroupContacts == null) {
            liveDataGroupContacts = new MutableLiveData<>();

        }
        getGroupOfContacts();
        return liveDataGroupContacts;
    }

    private void getGroupOfContacts() {
        disposable.add(useCase.getAllGroupOfContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groupContacts -> {
                    liveDataGroupContacts.postValue(groupContacts);
                }, throwable -> {
                    Log.d("Throwable get Group ", throwable.getMessage());
                }));
    }

    //Получаем все контакты конкретной группы при первичной загрузке фрагмента контактов
    public LiveData<List<Contact>> getAllContacts(String nameGroup) {
        liveDataContacts = new MutableLiveData<>();
        getAllContactsOfGroup(nameGroup);
        return liveDataContacts;
    }

    private void getAllContactsOfGroup(String nameGroup) {
        disposable.add(useCase.getAllContactsOfGroup(nameGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    liveDataContacts.postValue(contacts);
                }, throwable -> {
                    Log.d("Throwable get Contacts of Group ", throwable.getMessage());
                }));

    }

    //Возвращает все subgroup
    public LiveData<List<SubGroupContact>> getAllSubGroup(String nameGroup) {
        if (liveDataSubGroupContact == null) {
            liveDataSubGroupContact = new MutableLiveData<>();
        }
        disposable.add(useCase.getAllSubGroup(nameGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SubGroupContact>>() {
                    @Override
                    public void accept(List<SubGroupContact> subGroupContacts) throws Exception {
                        System.out.println(subGroupContacts.size());
                        liveDataSubGroupContact.postValue(subGroupContacts);
                    }
                }, throwable -> {
                    Log.d("Throwable get SubGroup", throwable.getMessage());
                }));
        return liveDataSubGroupContact;
    }


    public MutableLiveData<Boolean> createNewGroup(String nameGroup) {
        createBooleanLiveData();

        disposable.add(useCase.createNewGroup(nameGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }

    public LiveData<Boolean> createNewContact(String nameGroup, String nameSubGroup, String nameContact,
                                              String numberContact, int priorityContact, String description) {
        createBooleanLiveData();

        disposable.add(useCase.createNewContact(nameGroup, nameSubGroup, nameContact,
                numberContact, priorityContact, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }

    public LiveData<Boolean> createNewSubGroup(String nameGroup, String nameSubgroup) {
        createBooleanLiveData();
        disposable.add(useCase.createNewSubGroup(nameGroup, nameSubgroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));

        return booleanMutableLiveData;
    }

    public String getsSelectGroup() {
        return sSelectGroup;
    }

    public void setsSelectGroup(String sSelectGroup) {
        this.sSelectGroup = sSelectGroup;

    }

    public String getsSelectSubGroup() {
        return sSelectSubGroup;
    }

    public void setsSelectSubGroup(String sSelectSubGroup) {
        this.sSelectSubGroup = sSelectSubGroup;

    }


    public LiveData<Boolean> editNewGroup(String nameSelectedGroup, String newNameGroup) {
        createBooleanLiveData();
        disposable.add(useCase.editNameGroup(nameSelectedGroup, newNameGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }

    private void createBooleanLiveData() {
        if (booleanMutableLiveData == null) {
            booleanMutableLiveData = new MutableLiveData<>();
        }
    }
}
