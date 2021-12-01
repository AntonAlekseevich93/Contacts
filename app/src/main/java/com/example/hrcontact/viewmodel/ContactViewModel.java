package com.example.hrcontact.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hrcontact.DataRepository;
import com.example.hrcontact.db.entity.ContactWithGroups;
import com.example.hrcontact.model.SubGroupOfSelectGroup;
import com.example.hrcontact.support.ActionEnum;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ContactViewModel extends androidx.lifecycle.ViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private final DataRepository dataRepository;
    private ActionEnum actionForContacts;
    private MutableLiveData<List<ContactWithGroups>> liveDataContacts;
    private MutableLiveData<List<SubGroupOfSelectGroup>> liveDataGroupWithNestedSubGroup;
    private MutableLiveData<ContactWithGroups> liveDataContactEdit;
    private MutableLiveData<List<String>> ldListSelectedGroupAndSubgroup;
    private MutableLiveData<Boolean> ldBooleanMutableLiveData;
    private MutableLiveData<Map<Integer, String>> ldMapOfSelectedGroup;
    private MutableLiveData<Map<Integer, String>> ldMapOfSelectedSubGroup;
    private MutableLiveData<Map<Integer, Integer>> ldNumberOfSelectedSubgroupForAllGroup;
    private Map<Integer, String> mapOfGroup = new HashMap<>();
    private Map<Integer, String> mapOfSubGroup = new HashMap<>();
    private Map<Integer, Integer> numberOfSelectedSubgroupForAllGroup = new HashMap<>();
    private int idEditableContact;


    public ContactViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    public void setBooleanLiveDataNull() {
        ldBooleanMutableLiveData = null;
    }

    public MutableLiveData<Boolean> createNewGroup(String nameGroup) {
        createBooleanLiveData();
        disposable.add(dataRepository.createNewGroup(nameGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    ldBooleanMutableLiveData.postValue(Boolean);
                }));
        return ldBooleanMutableLiveData;
    }


    public LiveData<Boolean> createNewSubGroup(int idGroup, String nameSubgroup) {
        createBooleanLiveData();
        disposable.add(dataRepository.createNewSubGroup(idGroup, nameSubgroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    ldBooleanMutableLiveData.postValue(Boolean);
                }));
        return ldBooleanMutableLiveData;
    }

    private void createBooleanLiveData() {
        if (ldBooleanMutableLiveData == null) {
            ldBooleanMutableLiveData = new MutableLiveData<>();
        }
    }


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
                .subscribe(groupContacts ->
                        liveDataGroupWithNestedSubGroup.postValue(groupContacts), throwable -> {
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
    public LiveData<Boolean> createNewContact(String nameContact, String numberContact,
                                              int priorityContact, String description) {
        createBooleanLiveData();
        disposable.add(dataRepository.createNewContact(mapOfGroup, mapOfSubGroup, nameContact,
                numberContact, priorityContact, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    ldBooleanMutableLiveData.postValue(Boolean);
                }));
        return ldBooleanMutableLiveData;
    }

    /**
     * Метод сохраняет отредактированный контакт в БД
     *
     * @param nameContact     Название контакта
     * @param numberContact   Номер контакта
     * @param priorityContact Приоритет контакта
     * @param description     Описание контакта
     * @return вернет true если контакт добавлен, false если номер был изменен и новый номер уже
     * существует
     */
    public LiveData<Boolean> saveEditedContact(String nameContact, String numberContact,
                                               int priorityContact, String description) {
        createBooleanLiveData();
        disposable.add(dataRepository.saveEditedContact(
                mapOfGroup, mapOfSubGroup, idEditableContact, nameContact,
                numberContact, priorityContact, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    ldBooleanMutableLiveData.postValue(Boolean);
                }));
        return ldBooleanMutableLiveData;
    }


    /**
     * Метод временно сохраняет выбранные пользователем группы и подгруппы при создании контакта
     *
     * @param id   выбранной подгруппы или группы
     * @param name выбранной подгруппы или группы
     * @param type 0 - группы, 1 - подгруппа
     */
    public void addNewSelectGroupOrSubGroup(Integer id, String name, Integer type) {
        if (mapOfGroup == null) mapOfGroup = new HashMap<>();
        if (mapOfSubGroup == null) mapOfSubGroup = new HashMap<>();
        if (type == 0) {
            if (mapOfGroup.containsKey(id)) mapOfGroup.remove(id);
            else mapOfGroup.put(id, name);
            ldMapOfSelectedGroup.setValue(mapOfGroup);
        } else if (type == 1) {
            if (mapOfSubGroup.containsKey(id))
                mapOfSubGroup.remove(id);
            else mapOfSubGroup.put(id, name);
            ldMapOfSelectedSubGroup.setValue(mapOfSubGroup);
        }
    }

    /**
     * @return Возвращает выбранные пользователем группы при создании контакта
     */
    public LiveData<Map<Integer, String>> getLdMapOfSelectedGroup() {
        if (ldMapOfSelectedGroup == null) {
            ldMapOfSelectedGroup = new MutableLiveData<>();
        }
        return ldMapOfSelectedGroup;
    }

    /**
     * @return Возвращает выбранные пользователем подгруппы при создании контакта
     */
    public LiveData<Map<Integer, String>> getLdMapOfSelectedSubGroup() {
        if (ldMapOfSelectedSubGroup == null) ldMapOfSelectedSubGroup = new MutableLiveData<>();
        return ldMapOfSelectedSubGroup;
    }

    public LiveData<Boolean> editNameGroupOrSubgroup(String name, String newName, int type) {
        createBooleanLiveData();
        disposable.add(dataRepository.editNameGroupOrSubgroup(name, newName, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Boolean -> {
                    ldBooleanMutableLiveData.postValue(Boolean);
                }));
        return ldBooleanMutableLiveData;
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
                        if (ldMapOfSelectedGroup == null)
                            ldMapOfSelectedGroup = new MutableLiveData<>();
                        ldMapOfSelectedGroup.postValue(mapOfGroup);
                        if (mapsListOfGroupAndSubgroup.size() > 1) {
                            mapOfSubGroup = mapsListOfGroupAndSubgroup.get(1);
                            if (ldMapOfSelectedSubGroup == null)
                                ldMapOfSelectedSubGroup = new MutableLiveData<>();
                            ldMapOfSelectedSubGroup.postValue(mapOfSubGroup);
                        }
                        parseMapOfSelectedGroupAndSubGroupToList();
                    }
                })
        );
    }


    /**
     * Функция удаляет контакт,группу, подгруппу из БД
     *
     * @param actionEnum удаление контакта, группы или подгруппы
     * @param idDelete   id удаляемого объекта
     */
    public void delete(@NotNull ActionEnum actionEnum, int idDelete) {
        dataRepository.delete(actionEnum, idDelete);
    }


    /**
     * Метод изменяет в Map количество выбранных подгрупп для конкретной группы
     *
     * @param idGroup id группы для которой изменяется кол-во
     * @param action  если true - кол-во увеличивается на 1, если false - уменьшается
     */
    public void changeNumberOfSelectedGroup(Integer idGroup, Boolean action) {
        if (numberOfSelectedSubgroupForAllGroup == null)
            numberOfSelectedSubgroupForAllGroup = new HashMap<>();
        if (numberOfSelectedSubgroupForAllGroup.containsKey(idGroup)) {
            int count = numberOfSelectedSubgroupForAllGroup.get(idGroup);
            if (action) count++;
            else count--;
            numberOfSelectedSubgroupForAllGroup.put(idGroup, count);
        } else {
            numberOfSelectedSubgroupForAllGroup.put(idGroup, 1);
        }
    }

    public LiveData<Map<Integer, Integer>> getAmountSelectedSubgroup() {
        if (ldNumberOfSelectedSubgroupForAllGroup == null)
            ldNumberOfSelectedSubgroupForAllGroup = new MutableLiveData<>();
        return ldNumberOfSelectedSubgroupForAllGroup;
    }

    /**
     * Функция сохраняет кол-во выбранных подгрупп для каждой группы в LiveData
     */
    public void saveAmountSelectedSubGroup() {
        ldNumberOfSelectedSubgroupForAllGroup.setValue(numberOfSelectedSubgroupForAllGroup);
    }

    public void clearDataPopBackStack() {
        ldMapOfSelectedGroup = null;
        ldMapOfSelectedSubGroup = null;
        mapOfGroup = null;
        mapOfSubGroup = null;
        numberOfSelectedSubgroupForAllGroup = null;
        ldListSelectedGroupAndSubgroup = null;
        ldNumberOfSelectedSubgroupForAllGroup = null;
    }

    public boolean ifGroupAdded() {
        if (mapOfGroup != null && mapOfGroup.size() > 0)
            return true;
        else return false;
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

}
