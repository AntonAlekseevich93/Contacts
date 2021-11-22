package com.example.contacts.screens.createContactFragment.selectGroupAndSubgroupFragment;

import android.content.Context;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.data.relation.SubGroupOfSelectGroup;
import com.example.contacts.screens.dialogFragmentContact.DialogFragmentContacts;
import com.example.contacts.screens.createContactFragment.selectGroupAndSubgroupFragment.adapter.SelectGroupAdapter;
import com.example.contacts.screens.viewmodel.ContactViewModel;
import com.example.contacts.support.ActionEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Function3;

public class SelectGroupAndSubgroupFragment extends Fragment {
    private RecyclerView recyclerView;
    private SelectGroupAdapter adapter;
    private List<SubGroupOfSelectGroup> listGroup = new ArrayList<>();
    private ContactViewModel viewModel;
    private LinearLayout layoutCreateNewGroup;
    //Хранит выбранные пользователем группы (id, name)
    private Map<Integer, String> tempMapOfSelectedGroup = new HashMap<>();
    //Хранит выбранные пользователем подгруппы (id, name)
    private Map<Integer, String> tempMapOfSelectedSubGroup = new HashMap<>();
    private ImageView imageViewSaveSelectedGroupsAndSubGroupsForNewContact;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_select_group_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewSelectGroup);
        layoutCreateNewGroup = view.findViewById(R.id.linearLayoutCreateGroup);

        imageViewSaveSelectedGroupsAndSubGroupsForNewContact = view.findViewById(R.id.imageViewSaveSelectedGroupForNewContact);

        ImageView imageViewCancelSelectGroup = view.findViewById(R.id.imageViewCancelSelectGroupForCreateContact);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        /**
         * Функция получает выбранные пользователем группы и передает их в адаптер для установки
         * выбранных значений
         */
        LiveData<Map<Integer, String>> getSelectedGroup = viewModel.getMapOfSelectedGroup();
        getSelectedGroup.observe(getViewLifecycleOwner(), map1 -> {
            if (map1 != null) {
                adapter.setMapOfSelectedGroup(map1);
            }
        });

        /**
         * Функция получает выбранные пользователем подгруппы и передает их в адаптер для установки
         * выбранных значений
         */
        LiveData<Map<Integer, String>> getSelectedSubGroup = viewModel.getMapOfSelectedSubGroup();
        getSelectedSubGroup.observe(getViewLifecycleOwner(), map1 -> {

            if (map1 != null) {
                adapter.setMapOfSelectedSubGroup(map1);
            }
        });


        /**
         * Function3 Принимает данные от GroupAdapter и SubgroupAdapter
         * 1 параметр - id группы или подгруппы
         * 2 параметр - имя группы или подгруппы
         * 3 параметр - тип, где 0 - группа, 1 - подгруппа
         */
        adapter = new SelectGroupAdapter(view.getContext(), listGroup, new Function3<Integer, String, Integer, Void>() {
            @NonNull
            @Override
            public Void apply(@NonNull Integer id, @NonNull String name, @NonNull Integer type) throws Exception {
                viewModel.addNewSelectGroupOrSubGroup(id, name, type);
                return null;
            }
        }, integer -> {
            /**
             * функция создает DialogFragment для создания новой подгруппы
             */
            new DialogFragmentContacts(viewModel, null, integer, ActionEnum.CREATE_NEW_SUB_GROUP)
                    .show(getChildFragmentManager(), DialogFragmentContacts.TAG);
            return null;
        });

        /**
         * Метод вызывается при нажатии на кнопку "сохранить" во фрагменте выбора групп и подгрупп
         */
        imageViewSaveSelectedGroupsAndSubGroupsForNewContact.setOnClickListener(view1 -> {
//            viewModel.setMapOfSelectedGroup(tempMapOfSelectedGroup);
            viewModel.parseMapOfSelectedGroupAndSubGroupToList();
            SelectGroupAndSubgroupFragment.this.getParentFragmentManager().popBackStack();
        });


        recyclerView.setAdapter(adapter);


//        LiveData<Map<Integer, String>> getSelectedGroup = viewModel.getMapOfSelectedGroup();
//        getSelectedGroup.observe(getViewLifecycleOwner(), map1 -> {
//            if (map1 != null) {
//                List<GroupContacts> list = new ArrayList<>();
//                map = map1;
//                for(GroupContacts g : listGroup){
//                    if(map.containsKey(g.getId())) g.setSelect(true);
//                    list.add(g);
//                }
//                adapter.setList(list);
//            }
//        });

        /**
         * Метод создает DialogFragment для создания новой группы
         */
        layoutCreateNewGroup.setOnClickListener(view1 -> {
            new DialogFragmentContacts(viewModel, null, -1, ActionEnum.CREATE_NEW_GROUP).show(getChildFragmentManager(), DialogFragmentContacts.TAG);
        });

        /**
         * Получаем список групп и принадлежащих этой группе подгрупп
         */
        LiveData<List<SubGroupOfSelectGroup>> liveData = viewModel.getGroupAndSubgroupForSelected();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<SubGroupOfSelectGroup>>() {
            @Override
            public void onChanged(List<SubGroupOfSelectGroup> subGroupOfSelectGroups) {
                if (subGroupOfSelectGroups != null) {

                    adapter.setList(subGroupOfSelectGroups);
                }
            }
        });


        imageViewCancelSelectGroup.setOnClickListener(view1 -> {

            getParentFragmentManager().popBackStack();
        });


    }


}
