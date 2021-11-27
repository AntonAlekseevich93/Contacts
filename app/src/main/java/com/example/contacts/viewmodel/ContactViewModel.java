package com.example.contacts.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.contacts.db.entity.Contact;
import com.example.contacts.db.relation.SubGroupOfSelectGroup;
import com.example.contacts.db.entity.ContactWithGroups;
import com.example.contacts.db.entity.GroupContacts;
import com.example.contacts.db.entity.SubGroupContact;
import com.example.contacts.DataRepository;
import com.example.contacts.support.ActionEnum;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactViewModel extends androidx.lifecycle.ViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataRepository dataRepository;
    private ActionEnum actionForContacts;
    private MutableLiveData<List<ContactWithGroups>> liveDataContacts;
    private MutableLiveData<List<SubGroupOfSelectGroup>> liveDataGroupWithNestedSubGroup;
    private MutableLiveData<ContactWithGroups> liveDataContactEdit;
    private MutableLiveData<List<String>> ldListSelectedGroupAndSubgroup;
    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<Map<Integer, String>> mapOfSelectedGroup;
    private MutableLiveData<Map<Integer, String>> mapOfSelectedSubGroup;
    private Map<Integer, String> mapOfGroup = new HashMap<>();
    private Map<Integer, String> mapOfSubGroup = new HashMap<>();
    private int idEditableContact;


    public ContactViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    public MutableLiveData<Boolean> createNewGroup(String nameGroup) {
        createBooleanLiveData();

        disposable.add(dataRepository.createNewGroup(nameGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }


    public LiveData<Boolean> createNewSubGroup(int idGroup, String nameSubgroup) {
        createBooleanLiveData();
        disposable.add(dataRepository.createNewSubGroup(idGroup, nameSubgroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));

        return booleanMutableLiveData;
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

        disposable.add(dataRepository.getAllContacts(id, type)
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
        disposable.add(dataRepository.getAllGroupWithNestedSubGroup()
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
     * Метод сохраняет новый контакт в БД
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
        disposable.add(dataRepository.createNewContact(mapOfGroup, mapOfSubGroup, nameContact,
                numberContact, priorityContact, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }

    /**
     * Метод сохраняет отредактированный контакт в БД
     * @param nameContact Название контакта
     * @param numberContact Номер контакта
     * @param priorityContact Приоритет контакта
     * @param description Описание контакта
     * @return вернет true если контакт добавлен, false если номер был изменен и новый номер уже
     * существует
     */
    public LiveData<Boolean> saveEditedContact(String nameContact,
                                              String numberContact, int priorityContact, String description) {
        createBooleanLiveData();
        disposable.add(dataRepository.saveEditedContact(mapOfGroup, mapOfSubGroup, idEditableContact ,nameContact,
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
        disposable.add(dataRepository.editNameGroupOrSubgroup(name, newName, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    booleanMutableLiveData.postValue(Boolean);
                }));
        return booleanMutableLiveData;
    }

    /**
     * Получить действие с которым открыт фрагмент контакта
     *
     * @return ActionEnum.CREATE_CONTACT, ActionEnum.EDIT_CONTACT
     */
    public ActionEnum getActionForContacts() {
        return actionForContacts;
    }

    /**
     * Установить действие с которым открыт фрагмент контакта
     *
     * @param actionForContacts ActionEnum.CREATE_CONTACT, ActionEnum.EDIT_CONTACT
     */
    public void setActionForContacts(ActionEnum actionForContacts) {
        this.actionForContacts = actionForContacts;
    }

    /**
     * Функция возвращает контакт для редактирования
     *
     * @param idContact id выбранного контакта
     * @return Contact
     */
    public LiveData<ContactWithGroups> getSelectedContactForEdit(int idContact) {
        if (liveDataContactEdit == null) {
            liveDataContactEdit = new MutableLiveData<>();
        }
        disposable.add(dataRepository.getSelectedContactForEdit(idContact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactWithGroups -> {
                    if (contactWithGroups != null) {
                        liveDataContactEdit.postValue(contactWithGroups);
                        idEditableContact = contactWithGroups.getIdContacts();
                    }
                }, throwable -> {

                }));

        getMapOfCroupAndSubGroupEditableContact();

        return liveDataContactEdit;
    }

    /**
     * Функция получает и устанавливает значения в Map выбранных групп и подгруп для редактируемого
     * контакта
     */
    private void getMapOfCroupAndSubGroupEditableContact() {
        disposable.add(dataRepository.getMapGroupAndSubgroupThisContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mapsListOfGroupAndSubgroup -> {
                    if (mapsListOfGroupAndSubgroup != null && mapsListOfGroupAndSubgroup.size() > 0) {
                        mapOfGroup = mapsListOfGroupAndSubgroup.get(0);
                        if (mapOfSelectedGroup == null)
                            mapOfSelectedGroup = new MutableLiveData<>();
                        mapOfSelectedGroup.postValue(mapOfGroup);
                        if (mapsListOfGroupAndSubgroup.size() > 1) {
                            mapOfSubGroup = mapsListOfGroupAndSubgroup.get(1);
                            if (mapOfSelectedSubGroup == null)
                                mapOfSelectedSubGroup = new MutableLiveData<>();
                            mapOfSelectedSubGroup.postValue(mapOfSubGroup);
                        }
                        parseMapOfSelectedGroupAndSubGroupToList();
                    }
                })
        );
    }


    /**
     * Функция удаляет контакт,группу, подгруппу из БД
     * @param actionEnum удаление контакта, группы или подгруппы
     * @param idDelete id удаляемого объекта
     */
    public void delete(@NotNull ActionEnum actionEnum, int idDelete) {
        dataRepository.delete(actionEnum, idDelete);
    }
}
