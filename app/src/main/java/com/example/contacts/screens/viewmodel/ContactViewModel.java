package com.example.contacts.screens.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.contacts.data.relation.SubGroupOfSelectGroup;
import com.example.contacts.entity.ContactWithGroups;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;
import com.example.contacts.iteractor.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactViewModel extends ViewModel {
    private MutableLiveData<List<GroupContacts>> liveDataGroupContacts;
    private MutableLiveData<List<ContactWithGroups>> liveDataContacts;
    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<Map<Integer, String>> mapOfSelectedGroup;
    private MutableLiveData<Map<Integer, String>> mapOfSelectedSubGroup;
    private MutableLiveData<List<SubGroupContact>> liveDataSubGroupContact;
    private UseCase useCase;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String sSelectGroup = "";
    private String sSelectSubGroup = "";
    private Map<Integer, String> mapOfGroup = new HashMap<>();
    private Map<Integer, String> mapOfSubGroup = new HashMap<>();
    private MutableLiveData<List<String>> ldListSelectedGroupAndSubgroup;


    private MutableLiveData<List<SubGroupOfSelectGroup>> liveDataGroupWithNestedSubGroup;


    public ContactViewModel(UseCase useCase) {
        this.useCase = useCase;
    }


    private void getGroupOfContacts() {

    }

    //Не нужен
//    public LiveData<List<GroupContacts>> getAllGroupContacts() {
//        if (liveDataGroupContacts == null) {
//            liveDataGroupContacts = new MutableLiveData<>();
//
//        }
//        disposable.add(useCase.getAllGroupOfContacts()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<GroupContacts>>() {
//                    @Override
//                    public void accept(List<GroupContacts> groupContacts) throws Exception {
//                        if (mapOfGroup != null) {
//                            for (int i = 0; i < groupContacts.size(); i++) {
//                                if (mapOfGroup.containsKey(groupContacts.get(i).getId()))
//                                    groupContacts.get(i).setSelect(true);
//                            }
//                        }
//                        liveDataGroupContacts.postValue(groupContacts);
//                    }
//                }, throwable -> {
//                    Log.d("Throwable get Group ", throwable.getMessage());
//                }));
//        return liveDataGroupContacts;
//    }

//    private void getAllContactsOfGroup(String nameGroup) {
//
//
//    }

    //Возвращает все subgroup
//    public LiveData<List<SubGroupContact>> getAllSubGroup(String nameGroup) {
//        if (liveDataSubGroupContact == null) {
//            liveDataSubGroupContact = new MutableLiveData<>();
//        }
//        disposable.add(useCase.getAllSubGroup(nameGroup)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<SubGroupContact>>() {
//                    @Override
//                    public void accept(List<SubGroupContact> subGroupContacts) throws Exception {
//                        System.out.println(subGroupContacts.size());
//                        liveDataSubGroupContact.postValue(subGroupContacts);
//                    }
//                }, throwable -> {
//                    Log.d("Throwable get SubGroup", throwable.getMessage());
//                }));
//        return liveDataSubGroupContact;
//    }


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


    public LiveData<Boolean> createNewSubGroup(int idGroup, String nameSubgroup) {
        createBooleanLiveData();
        disposable.add(useCase.createNewSubGroup(idGroup, nameSubgroup)
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


//

    private void createBooleanLiveData() {
        if (booleanMutableLiveData == null) {
            booleanMutableLiveData = new MutableLiveData<>();
        }
    }


    // ОТ СЮДА НОВЫЕ


    /**
     * Метод получает список контактов выбранной группы
     *
     * @param id   - id группы или подгруппы
     * @param type - 0 - группа, 1 - подгруппа
     * @return Возвращает список контактов
     */
    public LiveData<List<ContactWithGroups>> getAllContacts(int id, int type) {
        if (liveDataContacts == null) liveDataContacts = new MutableLiveData<>();

        disposable.add(useCase.getAllContacts(id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    liveDataContacts.postValue(contacts);
                }, throwable -> {
                    Log.d("Throwable get Contacts of Group ", throwable.getMessage());
                }));

        return liveDataContacts;
    }



    /**
     * Метод получает список групп и принадлежащих этой группе подгрупп
     */
    public void getAllGroupContactsWithNestedSubGroupFromDB() {
        if (liveDataGroupWithNestedSubGroup == null)
            liveDataGroupWithNestedSubGroup = new MutableLiveData<>();
        disposable.add(useCase.getAllGroupWithNestedSubGroup()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SubGroupOfSelectGroup>>() {
                    @Override
                    public void accept(List<SubGroupOfSelectGroup> groupContacts) throws Exception {
                        liveDataGroupWithNestedSubGroup.postValue(groupContacts);
                    }
                }, throwable -> {
                    Log.d("Throwable get Group ", throwable.getMessage());
                }));
    }


    public LiveData<List<SubGroupOfSelectGroup>> getGroupAndSubgroupForSelected() {
        return liveDataGroupWithNestedSubGroup;
    }

    /**
     * Функция добавляет из Map в List названия выбранных групп и подгруп и устанавливает их
     * в LiveData
     */
    public void parseMapOfSelectedGroupAndSubGroupToList() {
        List<String> listNamesGroupAndSubgroup = new ArrayList<>();
        if (mapOfGroup != null) {
            for (Integer key : mapOfGroup.keySet()) {
                listNamesGroupAndSubgroup.add(mapOfGroup.get(key));
            }
        }
        if (mapOfSubGroup != null) {
            for (Integer key : mapOfSubGroup.keySet()) {
                listNamesGroupAndSubgroup.add(mapOfSubGroup.get(key));
            }
        }
        ldListSelectedGroupAndSubgroup.setValue(listNamesGroupAndSubgroup);
    }

    /**
     * @return Возвращает Список названий выбранных групп и подгрупп
     */
    public LiveData<List<String>> getNameOfSelectedGroupAndSubgroup() {
        if (ldListSelectedGroupAndSubgroup == null)
            ldListSelectedGroupAndSubgroup = new MutableLiveData<>();
        return ldListSelectedGroupAndSubgroup;
    }

    /**
     * Метод сохраняет контакт в БД
     *
     * @param nameContact     Название контакта
     * @param numberContact   Номер контакта
     * @param priorityContact Приоритет контакта
     * @param description     Описание контакта
     * @return вернет true если контакт добавлен, false если такой контакт существует
     */
    public LiveData<Boolean> createNewContact(String nameContact,
                                              String numberContact, int priorityContact, String description) {
        createBooleanLiveData();
        disposable.add(useCase.createNewContact(mapOfGroup, mapOfSubGroup, nameContact,
                numberContact, priorityContact, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }

    /**
     * Метод временно сохраняет выбранные пользователем группы и подгруппы при создании контакта
     *
     * @param id   выбранной подгруппы или группы
     * @param name выбранной подгруппы или группы
     * @param type 0 - группы, 1 - подгруппа
     */
    public void addNewSelectGroupOrSubGroup(Integer id, String name, Integer type) {
        if (type == 0) {
            if (mapOfGroup.containsKey(id)) mapOfGroup.remove(id);
            else mapOfGroup.put(id, name);
            mapOfSelectedGroup.setValue(mapOfGroup);
        } else if (type == 1) {
            if (mapOfSubGroup.containsKey(id))
                mapOfSubGroup.remove(id);
            else mapOfSubGroup.put(id, name);
            mapOfSelectedSubGroup.setValue(mapOfSubGroup);
        }
    }

    /**
     * @return Возвращает выбранные пользователем группы при создании контакта
     */
    public LiveData<Map<Integer, String>> getMapOfSelectedGroup() {
        if (mapOfSelectedGroup == null) {
            mapOfSelectedGroup = new MutableLiveData<>();
        }
        return mapOfSelectedGroup;
    }

    /**
     * @return Возвращает выбранные пользователем подгруппы при создании контакта
     */
    public LiveData<Map<Integer, String>> getMapOfSelectedSubGroup() {
        if (mapOfSelectedSubGroup == null) mapOfSelectedSubGroup = new MutableLiveData<>();
        return mapOfSelectedSubGroup;
    }


    public LiveData<Boolean> editNameGroupOrSubgroup(String name, String newName, int type) {
        createBooleanLiveData();
        disposable.add(useCase.editNameGroupOrSubgroup(name, newName, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }

}
