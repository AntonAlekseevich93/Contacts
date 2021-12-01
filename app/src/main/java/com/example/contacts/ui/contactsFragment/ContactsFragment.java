package com.example.contacts.ui.contactsFragment;

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
import com.example.contacts.db.entity.ContactWithGroups;
import com.example.contacts.support.ActionEnum;
import com.example.contacts.support.IClickListenerUpdateData;
import com.example.contacts.ui.DeleteFragment;
import com.example.contacts.ui.InfoContactFragment;
import com.example.contacts.ui.MainFragment.MainFragment;
import com.example.contacts.ui.contactsFragment.adapter.ContactsAdapter;
import com.example.contacts.ui.createContactFragment.CreateContactFragment;
import com.example.contacts.viewmodel.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements IClickListenerUpdateData {
    private ContactViewModel contactViewModel;
    private int idGroup = -1;
    private int typeGroupOrSubgroup = -1;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<ContactWithGroups> listOfContacts = new ArrayList<>();
    private TextView tvToolbarContactInfo;

    public static final String BUNDLE_ID_CONTACT_FOR_EDIT = "bundle.id.contact.for.edit";
    private FloatingActionButton fabAddNewContact;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        idGroup = getArguments().getInt(MainFragment.PARAM_BUNDLE_KEY_ID);
        typeGroupOrSubgroup = getArguments().getInt(MainFragment.PARAM_BUNDLE_KEY_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_show_group_with_nested_subgroup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvToolbarContactInfo = view.findViewById(R.id.tvNameToolbar);
        tvToolbarContactInfo.setText(R.string.toolbar_name_contact);
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAddNewContact = view.findViewById(R.id.fabAddContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ContactsAdapter(view.getContext(), listOfContacts,
                /**
                 * функция запускает фрагмент редактирования контакта
                 */
                idSelectContact -> {
                    contactViewModel.setActionForContacts(ActionEnum.EDIT_CONTACT);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.containerGroup, getFragmentWithSetsArguments(idSelectContact))
                            .addToBackStack(null)
                            .commit();
                    return null;
                },
                /**
                 * функция запускает фрагмент информации о контакте
                 */
                (nameContact, descriptionContact) -> {
                    new InfoContactFragment(nameContact, descriptionContact)
                            .show(getChildFragmentManager(), InfoContactFragment.TAG);
                    return null;
                }
                /**
                 * Функция запускает фрагмент удаления контакта
                 */
                , idContact -> {
            startFragmentDeleteContact(ActionEnum.DELETE_CONTACT, idContact, 0);
            return null;
        });
        recyclerView.setAdapter(adapter);

        CreateContactFragment createContactFragment = new CreateContactFragment();

        fabAddNewContact.setOnClickListener(v -> {
            contactViewModel.setActionForContacts(ActionEnum.CREATE_CONTACT);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.containerGroup, createContactFragment)
                    .addToBackStack(null)
                    .commit();
        });

        getAllContacts(idGroup, typeGroupOrSubgroup);
    }

    @Override
    public void onDestroy() {
        //Очищаем лист, чтобы старые данные не попадали в другую группу пока получаем новые из БД
        adapter.clearList();
        super.onDestroy();
    }

    private CreateContactFragment getFragmentWithSetsArguments(int idContact) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_CONTACT_FOR_EDIT, idContact);
        CreateContactFragment editFragment = new CreateContactFragment();
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

    private void startFragmentDeleteContact(ActionEnum actionEnum, int id, int type) {
        DeleteFragment deleteFragment = new DeleteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DeleteFragment.TAG_ACTION, actionEnum);
        bundle.putInt(DeleteFragment.TAG_ID, id);
        bundle.putInt(DeleteFragment.TAG_TYPE, type);
        deleteFragment.setArguments(bundle);
        deleteFragment.show(getChildFragmentManager(), DeleteFragment.TAG);
    }
}
