package com.example.contacts.screens.contactFragment;

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
import com.example.contacts.screens.contactFragment.adapter.ContactAdapter;
import com.example.contacts.entity.Contact;
import com.example.contacts.screens.groupFragment.GroupFragment;
import com.example.contacts.screens.viewmodel.ContactViewModel;

import java.util.List;

public class ContactFragment extends Fragment {
    private ContactViewModel viewModel;
    private String sGroupName = "";
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> listOfContacts;
    private TextView tvToolbarContactInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        sGroupName = getArguments().getString(GroupFragment.PARAM_BUNDLE_KEY1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_group_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvToolbarContactInfo = view.findViewById(R.id.tvNameToolbar);
        tvToolbarContactInfo.setText(R.string.toolbar_name_contact);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ContactAdapter(view.getContext(), listOfContacts);
        recyclerView.setAdapter(adapter);

        LiveData<List<Contact>> liveData = viewModel.getAllContacts(sGroupName);
        liveData.observe(getViewLifecycleOwner(), contacts -> {
            if (contacts != null) {

                adapter.setList(contacts);
            }
        });


    }


}
