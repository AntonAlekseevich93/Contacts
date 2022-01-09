package com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.model.SubGroupOfSelectGroup;
import com.example.hrcontact.support.ActionEnum;
import com.example.hrcontact.ui.DialogFragmentContacts;
import com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment.adapter.SelectGroupAdapter;
import com.example.hrcontact.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SelectGroupAndSubgroupFragment extends Fragment implements ISelectAdapterListeners {
    private SelectGroupAdapter adapter;
    private final List<SubGroupOfSelectGroup> listGroup = new ArrayList<>();
    private ContactViewModel contactViewModel;
    private LinearLayout layoutCreateNewGroup;
    private ImageView imageViewSaveSelectedGroupsAndSubGroupsForNewContact;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_select_group_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSelectGroup);
        layoutCreateNewGroup = view.findViewById(R.id.linearLayoutCreateGroup);

        imageViewSaveSelectedGroupsAndSubGroupsForNewContact =
                view.findViewById(R.id.imageViewSaveSelectedGroupForNewContact);

        ImageView imageViewCancelSelectGroup =
                view.findViewById(R.id.imageViewCancelSelectGroupForCreateContact);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        setSelectedGroupToAdapter();

        setSelectedSubGroupToAdapter();

        createAdapter(view);

        saveSelectedGroupAndSubgroup();

        recyclerView.setAdapter(adapter);

        setListenerForCreateNewGroup();

        getListGroupAndSubgroup();

        getAmountSelectedSubgroup();

        imageViewCancelSelectGroup.setOnClickListener(view1 -> {
            contactViewModel.clearDataPopBackStack();
            getParentFragmentManager().popBackStack();
        });
    }

    /**
     * Метод создает DialogFragment для создания новой группы
     */
    private void setListenerForCreateNewGroup() {
        layoutCreateNewGroup.setOnClickListener(view1 -> {
            startDialogFragmentForCreateNewGroup(ActionEnum.CREATE_NEW_GROUP);
        });
    }

    private void startDialogFragmentForCreateNewSubGroup(int i, ActionEnum actionEnum) {
        DialogFragmentContacts dialogFragmentContacts = new DialogFragmentContacts();
        Bundle bundle = new Bundle();
        bundle.putInt(DialogFragmentContacts.TAG_DIALOG_ID_GROUP, i);
        bundle.putSerializable(DialogFragmentContacts.TAG_DIALOG_ACTION_ENUM, actionEnum);
        dialogFragmentContacts.setArguments(bundle);
        dialogFragmentContacts.show(getChildFragmentManager(), DialogFragmentContacts.TAG);
    }

    private void startDialogFragmentForCreateNewGroup(ActionEnum actionEnum) {
        DialogFragmentContacts dialogFragmentContacts = new DialogFragmentContacts();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DialogFragmentContacts.TAG_DIALOG_ACTION_ENUM, actionEnum);
        dialogFragmentContacts.setArguments(bundle);
        dialogFragmentContacts.show(getChildFragmentManager(), DialogFragmentContacts.TAG);
    }

    /**
     * Функция получает выбранные пользователем группы и передает их в адаптер для установки
     * выбранных значений
     */
    private void setSelectedGroupToAdapter() {
        LiveData<Map<Integer, String>> getSelectedGroup = contactViewModel.getLdMapOfSelectedGroup();
        getSelectedGroup.observe(getViewLifecycleOwner(), map1 -> {
            if (map1 != null) {
                adapter.setMapOfSelectedGroup(map1);
            }
        });
    }

    /**
     * Функция получает выбранные пользователем подгруппы и передает их в адаптер для установки
     * выбранных значений
     */
    private void setSelectedSubGroupToAdapter() {
        LiveData<Map<Integer, String>> getSelectedSubGroup = contactViewModel.getLdMapOfSelectedSubGroup();
        getSelectedSubGroup.observe(getViewLifecycleOwner(), map1 -> {
            if (map1 != null) {
                adapter.setMapOfSelectedSubGroup(map1);
            }
        });
    }

    private void createAdapter(View view) {
        adapter = new SelectGroupAdapter(view.getContext(), listGroup, this);
    }

    /**
     * Метод вызывается при нажатии на кнопку "сохранить" во фрагменте выбора групп и подгрупп
     */
    private void saveSelectedGroupAndSubgroup() {
        imageViewSaveSelectedGroupsAndSubGroupsForNewContact.setOnClickListener(view1 -> {
            contactViewModel.parseMapOfSelectedGroupAndSubGroupToList();
            contactViewModel.saveAmountSelectedSubGroup();
            SelectGroupAndSubgroupFragment.this.getParentFragmentManager().popBackStack();
        });
    }

    /**
     * Получаем список групп и принадлежащих этой группе подгрупп
     */
    private void getListGroupAndSubgroup() {
        LiveData<List<SubGroupOfSelectGroup>> liveData =
                contactViewModel.getGroupAndSubgroupForSelected();
        liveData.observe(getViewLifecycleOwner(), subGroupOfSelectGroups -> {
            if (subGroupOfSelectGroups != null) {
                adapter.setList(subGroupOfSelectGroups);
            }
        });
    }

    /**
     * Метод сохраняет кол-во выбранных подгрупп для конкретной группы
     *
     * @param idGroup - id группы для которой сохраняется кол-во выбранных подгрупп
     * @param action  если true - кол-во увелчивается, false - уменьшается.
     */
    @Override
    public void setCountSelectedGroup(Integer idGroup, Boolean action) {
        contactViewModel.changeNumberOfSelectedGroup(idGroup, action);
    }

    /**
     * Принимает данные от GroupAdapter и SubgroupAdapter выбранных групп и подгрупп
     * 1 параметр - id группы или подгруппы
     * 2 параметр - имя группы или подгруппы
     * 3 параметр - тип, где 0 - группа, 1 - подгруппа
     */
    @Override
    public void setIdAndNameFromSelectedGroupOrSubgroup(Integer id, String name, Integer type) {
        contactViewModel.addNewSelectGroupOrSubGroup(id, name, type);
    }

    /**
     * функция создает DialogFragment для создания новой подгруппы
     */
    @Override
    public void createNewSubgroup(Integer idGroup) {
        SelectGroupAndSubgroupFragment.this
                .startDialogFragmentForCreateNewSubGroup(idGroup, ActionEnum.CREATE_NEW_SUB_GROUP);
    }

    private void getAmountSelectedSubgroup() {

        LiveData<Map<Integer, Integer>> ldAmount = contactViewModel.getAmountSelectedSubgroup();
        ldAmount.observe(getViewLifecycleOwner(), integerIntegerMap -> {
            if (integerIntegerMap != null) {
                adapter.setAmountSelectedSubgroup(integerIntegerMap);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        contactViewModel.saveAmountSelectedSubGroup();
        super.onSaveInstanceState(outState);
    }
}
