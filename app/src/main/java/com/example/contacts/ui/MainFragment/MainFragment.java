package com.example.contacts.ui.MainFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.db.relation.SubGroupOfSelectGroup;
import com.example.contacts.db.entity.GroupContacts;
import com.example.contacts.ui.createContactFragment.CreateContactFragment;
import com.example.contacts.ui.contactsFragment.ContactsFragment;
import com.example.contacts.ui.DialogFragmentContacts;
import com.example.contacts.ui.MainFragment.adapter.MainAdapterGroup;
import com.example.contacts.viewmodel.ContactViewModel;
import com.example.contacts.support.ActionEnum;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private MainAdapterGroup adapter;
    private List<GroupContacts> listOfContact = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;
    private FloatingActionButton fabAddNewContact;
    public static final String PARAM_BUNDLE_KEY_ID = "param1";
    public static final String PARAM_BUNDLE_KEY_TYPE = "param2";
    private TextView tvNameToolbar;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getParentFragmentManager();
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        adapter = new MainAdapterGroup(context, listOfContact,
/**
 * Функция возвращает id (type 0 = group, 1 = subgroup) и открывает фрагмент с контактами данной группы
 */
                (id, type) -> {
                    MainFragment.this.openGroupOfContact(id, type);
                    return null;
                },
                /**
                 * Функиця возвращает имя группы или подгруппы для изменения имени
                 * и тип, где 0 - группа, 1 - подгруппа
                 */
                (name, type) -> {
                    if (type == 0)
                        new DialogFragmentContacts(contactViewModel, name,
                                ActionEnum.EDIT_GROUP).show(MainFragment.this.getChildFragmentManager(),
                                DialogFragmentContacts.TAG);
                    else if (type == 1)
                        new DialogFragmentContacts(contactViewModel, name,
                                ActionEnum.EDIT_SUB_GROUP).show(MainFragment.this.getChildFragmentManager(),
                                DialogFragmentContacts.TAG);
                    return null;
                });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.main_fragment_show_group_with_nested_subgroup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabAddNewContact = view.findViewById(R.id.fabAddContact);
        tvNameToolbar = view.findViewById(R.id.tvNameToolbar);
        tvNameToolbar.setText(R.string.toolbar_name_group);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        CreateContactFragment createContactFragment = new CreateContactFragment();

        getAllGroupWithNestedSubgroupSetToAdapter();

        fabAddNewContact.setOnClickListener(v -> {
            contactViewModel.setActionForContacts(ActionEnum.CREATE_CONTACT);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, createContactFragment)
                    .addToBackStack(null)
                    .commit();
        });


    }

    /**
     * Функция открывает фрагмент с контактами выбранной группы
     *
     * @param id id выбранной группы
     */
    private void openGroupOfContact(Integer id, int type) {
        ContactsFragment contactsFragment = new ContactsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(PARAM_BUNDLE_KEY_ID, id);
        arguments.putInt(PARAM_BUNDLE_KEY_TYPE, type);
        contactsFragment.setArguments(arguments);

        fragmentManager.beginTransaction()
                .replace(R.id.containerGroup, contactsFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Функция получает все группы и вложенные в них подгруппы для вывода на экран
     */
    private void getAllGroupWithNestedSubgroupSetToAdapter() {
        contactViewModel.getAllGroupContactsWithNestedSubGroupFromDB();
        LiveData<List<SubGroupOfSelectGroup>> liveData = contactViewModel.getGroupAndSubgroupForSelected();
        liveData.observe(getViewLifecycleOwner(), subGroupOfSelectGroups -> {
            if (subGroupOfSelectGroups != null) {

                adapter.setListGroupAndSubgroup(subGroupOfSelectGroups);
            }

        });
    }

}
