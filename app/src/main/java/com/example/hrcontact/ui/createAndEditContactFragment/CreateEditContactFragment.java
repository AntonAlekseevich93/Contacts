package com.example.hrcontact.ui.createAndEditContactFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.db.entity.ContactWithGroups;
import com.example.hrcontact.support.ActionEnum;
import com.example.hrcontact.ui.contactsFragment.ContactsFragment;
import com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment.SelectGroupAndSubgroupFragment;
import com.example.hrcontact.viewmodel.ContactViewModel;

import java.util.List;

public class CreateEditContactFragment extends Fragment {
    private ContactViewModel contactViewModel;
    private TextView tvSelectGroup;
    private EditText edtNameContact;
    private EditText edtNumberContact;
    private Spinner spinnerPriorityContact;
    private EditText edtDescriptionContact;
    private ImageView imageViewSaveContact;
    private FragmentManager fragmentManager;
    private AdapterCreateContactFragment adapterGroup;
    private ActionEnum actionForThisFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        fragmentManager = getParentFragmentManager();

        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                contactViewModel.clearDataPopBackStack();
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_create_contact_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        actionForThisFragment = contactViewModel.getActionForContacts();
        //получаем группы и подгруппы для выбора
        contactViewModel.getAllGroupContactsWithNestedSubGroupFromDB();
        RecyclerView recyclerViewGroup = view.findViewById(R.id.recyclerViewSelectGroupForCreateContact);
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterGroup = new AdapterCreateContactFragment(view.getContext());
        recyclerViewGroup.setAdapter(adapterGroup);

        ifThisEditContactAction(view);

        tvSelectGroup.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, new SelectGroupAndSubgroupFragment())
                    .addToBackStack(null)
                    .commit();
        });

        imageViewSaveContact.setOnClickListener(v -> {
            if (actionForThisFragment == ActionEnum.CREATE_CONTACT) addNewContact();
            else if (actionForThisFragment == ActionEnum.EDIT_CONTACT) editContact();
        });
        setSelectGroupAndSubgroupToAdapter();
    }


    private void initializeViews(View view) {
        tvSelectGroup = view.findViewById(R.id.tvSelectGroup);
        edtNameContact = view.findViewById(R.id.editTextTextPersonName);
        edtNumberContact = view.findViewById(R.id.editTextTextPersonName2);
        spinnerPriorityContact = view.findViewById(R.id.spinnerSelectPriorityContact);
        edtDescriptionContact = view.findViewById(R.id.editTextAddDescriptionContact);
        imageViewSaveContact = view.findViewById(R.id.imageViewSaveContact);
    }

    /**
     * Функция добавляет новый контакт в БД
     */
    private void addNewContact() {
        String nameContact = edtNameContact.getText().toString();
        String numberContact = edtNumberContact.getText().toString();
        int priority = spinnerPriorityContact.getSelectedItemPosition();
        String description = edtDescriptionContact.getText().toString();
        if (nameContact.length() <= 0 || numberContact.length() <= 0) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.toast_info_data_not_filled),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            LiveData<Boolean> liveData =
                    contactViewModel.createNewContact(nameContact, numberContact, priority, description);
            liveData.observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.toast_info_contact_added),
                            Toast.LENGTH_SHORT).show();
                    contactViewModel.setBooleanLiveDataNull();
                    contactViewModel.clearDataPopBackStack();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.toast_info_number_already_exist),
                            Toast.LENGTH_SHORT).show();
                    contactViewModel.setBooleanLiveDataNull();
                }
            });
        }
    }

    private void editContact() {
        String nameContact = edtNameContact.getText().toString();
        String numberContact = edtNumberContact.getText().toString();
        int priority = spinnerPriorityContact.getSelectedItemPosition();
        String description = edtDescriptionContact.getText().toString();
        if (nameContact.length() <= 0 || numberContact.length() <= 0) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.toast_info_data_not_filled),
                    Toast.LENGTH_SHORT).show();
        }
        LiveData<Boolean> liveData =
                contactViewModel.saveEditedContact(nameContact, numberContact, priority, description);
        liveData.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_info_contact_changed),
                        Toast.LENGTH_SHORT).show();
                contactViewModel.clearDataPopBackStack();
                contactViewModel.setBooleanLiveDataNull();
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_info_number_already_exist),
                        Toast.LENGTH_SHORT).show();
                contactViewModel.setBooleanLiveDataNull();
            }
        });
    }

    /**
     * Функция получает имена выбранных групп и подгрупп и устанавливает их в адаптер
     */
    private void setSelectGroupAndSubgroupToAdapter() {
        LiveData<List<String>> selectedGroupAndSubgroup =
                contactViewModel.getNameOfSelectedGroupAndSubgroup();
        selectedGroupAndSubgroup.observe(getViewLifecycleOwner(), s -> {
            adapterGroup.setNameSelectedGroupOrSubgroup(s);
        });
    }

    /**
     * Функция получает выбранный контакт для редактирования и устанавливает значения данного
     * контакта во View
     */
    private void ifThisEditContactAction(View view) {
        if (actionForThisFragment == ActionEnum.EDIT_CONTACT) {
            TextView tvToolbar = view.findViewById(R.id.toolbarName);
            tvToolbar.setText(view.getResources().getString(R.string.toolbar_name_edit_contact));
            assert getArguments() != null;
            int idContact = getArguments().getInt(ContactsFragment.BUNDLE_ID_CONTACT_FOR_EDIT);
            LiveData<ContactWithGroups> ldInfoAboutContact =
                    contactViewModel.getSelectedContactForEdit(idContact);
            ldInfoAboutContact.observe(getViewLifecycleOwner(), contactWithGroups -> {
                if (contactWithGroups != null) {
                    edtNameContact.setText(contactWithGroups.getName());
                    edtNumberContact.setText(contactWithGroups.getNumber());
                    spinnerPriorityContact.setSelection(contactWithGroups.getPriority());
                    edtDescriptionContact.setText(contactWithGroups.getDescription());
                }
            });
        }
    }


}
