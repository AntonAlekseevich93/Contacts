package com.example.contacts.screens.MainFragment;

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
import com.example.contacts.data.relation.SubGroupOfSelectGroup;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.screens.createContactFragment.CreateContactFragment;
import com.example.contacts.screens.contactsFragment.ContactsFragment;
import com.example.contacts.screens.dialogFragmentContact.DialogFragmentContacts;
import com.example.contacts.screens.MainFragment.adapter.MainAdapterGroup;
import com.example.contacts.screens.viewmodel.ContactViewModel;
import com.example.contacts.support.ActionEnum;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class MainFragment extends Fragment {
    private ContactViewModel viewModel;
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
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        adapter = new MainAdapterGroup(context, listOfContact,
/**
 * Функция возвращает id (type 0 = group, 1 = subgroup) и открывает фрагмент с контактами данной группы
 */
                (id, type) -> {
                    MainFragment.this.openGroupOfContact(id, type);
                    return null;
                },

                s -> {
            new DialogFragmentContacts(viewModel, s, -1, ActionEnum.EDIT_GROUP).show(getChildFragmentManager(), DialogFragmentContacts.TAG);
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




        viewModel.getAllGroupContactsWithNestedSubGroupFromDB();
        LiveData<List<SubGroupOfSelectGroup>> liveData = viewModel.getGroupAndSubgroupForSelected();
        liveData.observe(getViewLifecycleOwner(), subGroupOfSelectGroups -> {

            if (subGroupOfSelectGroups != null) {

                adapter.setListGroupAndSubgroup(subGroupOfSelectGroups);
            }

        });
        fabAddNewContact.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, new CreateContactFragment())
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


    //Создание новой группы
    private void createNewGroup(String nameNewGroup) {
//        LiveData<Boolean> booleanLiveData = viewModel.createNewGroup(nameNewGroup);
//        booleanLiveData.observe(getViewLifecycleOwner(), Boolean -> {
//            if (Boolean)
//                Toast.makeText(view.getContext(), getResources().getString(R.string.toast_message_group_success_created), Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(view.getContext(), getResources().getString(R.string.toast_message_group_exist), Toast.LENGTH_SHORT).show();
//        });
    }
}
