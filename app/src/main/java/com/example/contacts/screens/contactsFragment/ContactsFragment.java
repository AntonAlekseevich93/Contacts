package com.example.contacts.screens.contactsFragment;

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
import com.example.contacts.entity.ContactWithGroups;
import com.example.contacts.screens.contactsFragment.adapter.ContactsAdapter;
import com.example.contacts.entity.Contact;
import com.example.contacts.screens.MainFragment.MainFragment;
import com.example.contacts.screens.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private ContactViewModel viewModel;
    private int idGroup = -1;
    private int typeGroupOrSubgroup = -1;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<ContactWithGroups> listOfContacts = new ArrayList<>();
    private TextView tvToolbarContactInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ContactsAdapter(view.getContext(), listOfContacts);
        recyclerView.setAdapter(adapter);

        LiveData<List<ContactWithGroups>> liveData = viewModel.getAllContacts(idGroup, typeGroupOrSubgroup);
        liveData.observe(getViewLifecycleOwner(), contacts -> {
            if (contacts != null) {
                adapter.setList(contacts);
            }
        });
    }

    @Override
    public void onDestroy() {
        //Очищаем лист, чтобы старые данные не попадали в другую группу пока получаем новые из БД
        adapter.clearList();
        super.onDestroy();
    }
}
