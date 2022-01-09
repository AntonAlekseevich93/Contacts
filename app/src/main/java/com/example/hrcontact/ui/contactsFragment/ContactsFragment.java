package com.example.hrcontact.ui.contactsFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.db.entity.ContactWithGroups;
import com.example.hrcontact.support.ActionEnum;
import com.example.hrcontact.support.IClickListenerAdapterContact;
import com.example.hrcontact.ui.DeleteFragment;
import com.example.hrcontact.ui.InfoContactFragment;
import com.example.hrcontact.ui.MainFragment.MainFragment;
import com.example.hrcontact.ui.contactsFragment.adapter.ContactsAdapter;
import com.example.hrcontact.ui.createAndEditContactFragment.CreateEditContactFragment;
import com.example.hrcontact.viewmodel.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements IClickListenerAdapterContact {
    private ContactViewModel contactViewModel;
    private int idGroup = -1;
    private int typeGroupOrSubgroup = -1;
    private ContactsAdapter adapter;
    private final List<ContactWithGroups> listOfContacts = new ArrayList<>();
    public static final String BUNDLE_ID_CONTACT_FOR_EDIT = "bundle.id.contact.for.edit";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        idGroup = getArguments().getInt(MainFragment.PARAM_BUNDLE_KEY_ID);
        typeGroupOrSubgroup = getArguments().getInt(MainFragment.PARAM_BUNDLE_KEY_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.main_fragment_show_group_with_nested_subgroup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvToolbarContactInfo = view.findViewById(R.id.tvNameToolbar);
        tvToolbarContactInfo.setText(R.string.toolbar_name_contact);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton fabAddNewContact = view.findViewById(R.id.fabAddContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ContactsAdapter(view.getContext(), listOfContacts, this);
        recyclerView.setAdapter(adapter);

        CreateEditContactFragment createEditContactFragment = new CreateEditContactFragment();

        fabAddNewContact.setOnClickListener(v -> {
            contactViewModel.setActionForContacts(ActionEnum.CREATE_CONTACT);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.containerGroup, createEditContactFragment)
                    .addToBackStack(null)
                    .commit();
        });

        getAllContacts(idGroup, typeGroupOrSubgroup);
    }

    private CreateEditContactFragment getFragmentWithSetsArguments(int idContact) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_CONTACT_FOR_EDIT, idContact);
        CreateEditContactFragment editFragment = new CreateEditContactFragment();
        editFragment.setArguments(bundle);
        return editFragment;
    }

    private void getAllContacts(int idGroup, int typeGroupOrSubgroup) {
        LiveData<List<ContactWithGroups>> liveData = contactViewModel.getAllContacts(idGroup, typeGroupOrSubgroup);
        liveData.observe(getViewLifecycleOwner(), contacts -> {
            if (contacts != null) {
                adapter.setList(contacts);
            }
        });
    }

    @Override
    public void update() {
        getAllContacts(idGroup, typeGroupOrSubgroup);
    }

    /**
     * функция запускает фрагмент редактирования контакта
     */
    @Override
    public void openEditContact(int idContact) {
        contactViewModel.setActionForContacts(ActionEnum.EDIT_CONTACT);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.containerGroup, getFragmentWithSetsArguments(idContact))
                .addToBackStack(null)
                .commit();
    }

    /**
     * функция запускает фрагмент информации о контакте
     */
    @Override
    public void openInfoContact(String number, String nameContact, String descriptionContact) {
        new InfoContactFragment(number, nameContact, descriptionContact)
                .show(getChildFragmentManager(), InfoContactFragment.TAG);
    }

    /**
     * Функция запускает фрагмент удаления контакта
     */
    @Override
    public void deleteContact(int idContact) {
        startFragmentDeleteContact(ActionEnum.DELETE_CONTACT, idContact, 0);
    }

    /**
     * Функция позволяет поделиться контактом - отправляет интент с данными контакта
     *
     * @param number      номер контакта
     * @param nameContact имя контакта
     * @param description описание контакта
     */
    @Override
    public void shareContact(String number, String nameContact, String description) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String infoName = requireContext().getResources().getString(R.string.share_contact_name);
        String infoNumber = requireContext().getResources().getString(R.string.share_contact_number);
        String infoDescription =
                requireContext().getResources().getString(R.string.share_contact_description);
        String send = infoName + " " + nameContact + ", " +
                infoNumber + " " + number + ", " + infoDescription + " " + description;
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, send);
        Intent chosenIntent = Intent.createChooser(intent, "");
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(chosenIntent);
        }

    }


    private void startFragmentDeleteContact(ActionEnum actionEnum, int id, int type) {
        DeleteFragment deleteFragment = new DeleteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DeleteFragment.TAG_ACTION, actionEnum);
        bundle.putInt(DeleteFragment.TAG_ID, id);
        bundle.putInt(DeleteFragment.TAG_TYPE, type);
        deleteFragment.setArguments(bundle);
        deleteFragment.show(getChildFragmentManager(), DeleteFragment.TAG);
    }

    @Override
    public void onDestroy() {
        //Очищаем лист, чтобы старые данные не попадали в другую группу пока получаем новые из БД
        adapter.clearList();
        super.onDestroy();
    }
}
