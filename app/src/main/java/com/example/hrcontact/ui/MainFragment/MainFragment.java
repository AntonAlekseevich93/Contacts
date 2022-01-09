package com.example.hrcontact.ui.MainFragment;

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
import com.example.hrcontact.db.entity.GroupContacts;
import com.example.hrcontact.model.SubGroupOfSelectGroup;
import com.example.hrcontact.support.ActionEnum;
import com.example.hrcontact.support.BottomSheetDialogGroup;
import com.example.hrcontact.ui.MainFragment.adapter.MainAdapterGroup;
import com.example.hrcontact.ui.contactsFragment.ContactsFragment;
import com.example.hrcontact.ui.createAndEditContactFragment.CreateEditContactFragment;
import com.example.hrcontact.viewmodel.ContactViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements IMainAdapterListener {
    private ContactViewModel contactViewModel;
    private MainAdapterGroup adapter;
    private final List<GroupContacts> listOfContact = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;
    public static final String PARAM_BUNDLE_KEY_ID = "param1";
    public static final String PARAM_BUNDLE_KEY_NAME = "param2";
    public static final String PARAM_BUNDLE_KEY_TYPE = "param3";


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
        adapter = new MainAdapterGroup(context, listOfContact, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(
                R.layout.main_fragment_show_group_with_nested_subgroup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabAddNewContact = view.findViewById(R.id.fabAddContact);
        TextView tvNameToolbar = view.findViewById(R.id.tvNameToolbar);

        tvNameToolbar.setText(R.string.toolbar_name_group);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        CreateEditContactFragment createEditContactFragment = new CreateEditContactFragment();

        getAllGroupWithNestedSubgroupSetToAdapter();

        fabAddNewContact.setOnClickListener(v -> {
            contactViewModel.setActionForContacts(ActionEnum.CREATE_CONTACT);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, createEditContactFragment)
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
        LiveData<List<SubGroupOfSelectGroup>> liveData =
                contactViewModel.getGroupAndSubgroupForSelected();
        liveData.observe(getViewLifecycleOwner(), subGroupOfSelectGroups -> {
            if (subGroupOfSelectGroups != null) {
                adapter.setListGroupAndSubgroup(subGroupOfSelectGroups);
            }

        });
    }

    /**
     * Функция получает id группы или подгруппы
     * и type, где 0 = group, 1 = subgroup)
     * и открывает фрагмент с контактами данной группы/подгруппы
     */
    @Override
    public void openContactsForThisGroupOrSubgroup(int id, int type) {
        MainFragment.this.openGroupOfContact(id, type);
    }

    /**
     * Функиця получает
     * имя группы или подгруппы для изменения имени
     * тип, где 0 - группа, 1 - подгруппа
     * id группы или подгруппы
     */
    @Override
    public void editNameGroupOrSubgroup(String name, int type, int id) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_BUNDLE_KEY_NAME, name);
        bundle.putInt(PARAM_BUNDLE_KEY_TYPE, type);
        bundle.putInt(PARAM_BUNDLE_KEY_ID, id);
        BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetDialogGroup.newInstance();
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(MainFragment.this.getParentFragmentManager(),
                "BottomSheetGroup");
    }
}
