package com.example.contacts.ui.createContactFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.db.entity.ContactWithGroups;
import com.example.contacts.support.ActionEnum;
import com.example.contacts.ui.contactsFragment.ContactsFragment;
import com.example.contacts.ui.createContactFragment.selectGroupAndSubgroupFragment.SelectGroupAndSubgroupFragment;
import com.example.contacts.viewmodel.ContactViewModel;

import java.util.List;

public class CreateContactFragment extends Fragment {
    private ContactViewModel contactViewModel;
    private TextView tvSelectGroup;
    private EditText edtNameContact;
    private EditText edtNumberContact;
    private Spinner spinnerPriorityContact;
    private EditText edtDescriptionContact;
    private ImageView imageViewSaveContact;
    private FragmentManager fragmentManager;
    private RecyclerView recyclerViewGroup;
    private AdapterCreateContactFragment adapterGroup;
    private ActionEnum actionForThisFragment;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        fragmentManager = getParentFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_create_contact_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        actionForThisFragment = contactViewModel.getActionForContacts();
        contactViewModel.getAllGroupContactsWithNestedSubGroupFromDB();//получаем группы и подгруппы для выбора
        recyclerViewGroup = view.findViewById(R.id.recyclerViewSelectGroupForCreateContact);
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterGroup = new AdapterCreateContactFragment(view.getContext());
        recyclerViewGroup.setAdapter(adapterGroup);

        ifThisEditContactAction();

        tvSelectGroup.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, new SelectGroupAndSubgroupFragment())
                    .addToBackStack(null)
                    .commit();
        });

        imageViewSaveContact.setOnClickListener(v -> {
            if (actionForThisFragment == ActionEnum.CREATE_CONTACT) addNewContact();
           else if(actionForThisFragment == ActionEnum.EDIT_CONTACT) editContact();
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
            Toast.makeText(getContext(), "Введите данные", Toast.LENGTH_SHORT).show();
        } else {
            LiveData<Boolean> liveData = contactViewModel.createNewContact(nameContact, numberContact, priority, description);
            liveData.observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    Toast.makeText(getContext(), "Контакт добавлен", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else
                    Toast.makeText(getContext(), "Такой номер уже имеется", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void editContact() {
        String nameContact = edtNameContact.getText().toString();
        String numberContact = edtNumberContact.getText().toString();
        int priority = spinnerPriorityContact.getSelectedItemPosition();
        String description = edtDescriptionContact.getText().toString();
        if (nameContact.length() <= 0 || numberContact.length() <= 0) {
            Toast.makeText(getContext(), "Введите данные", Toast.LENGTH_SHORT).show();
        }LiveData<Boolean> liveData = contactViewModel.saveEditedContact(nameContact, numberContact, priority, description);
        liveData.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Toast.makeText(getContext(), "Контакт добавлен", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            } else
                Toast.makeText(getContext(), "Такой номер уже имеется", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Функция получает имена выбранных групп и подгрупп и устанавливает их в адаптер
     */
    private void setSelectGroupAndSubgroupToAdapter() {
        LiveData<List<String>> selectedGroupAndSubgroup = contactViewModel.getNameOfSelectedGroupAndSubgroup();
        selectedGroupAndSubgroup.observe(getViewLifecycleOwner(), s -> {
            adapterGroup.setNameSelectedGroupOrSubgroup(s);
        });
    }

    /**
     * Функция получает выбранный контакт для редактирования и устанавливает значения данного
     * контакта во View
     */
    private void ifThisEditContactAction() {
        if (actionForThisFragment == ActionEnum.EDIT_CONTACT) {
            assert getArguments() != null;
            int idContact = getArguments().getInt(ContactsFragment.BUNDLE_ID_CONTACT_FOR_EDIT);
            LiveData<ContactWithGroups> ldInfoAboutContact = contactViewModel.getSelectedContactForEdit(idContact);
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