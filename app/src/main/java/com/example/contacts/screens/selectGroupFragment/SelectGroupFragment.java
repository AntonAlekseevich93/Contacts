package com.example.contacts.screens.selectGroupFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.screens.dialogFragmentContact.DialogFragmentContacts;
import com.example.contacts.screens.selectGroupFragment.adapter.SelectGroupAdapter;
import com.example.contacts.screens.viewmodel.ContactViewModel;
import com.example.contacts.support.ActionEnum;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupFragment extends Fragment {
    private RecyclerView recyclerView;
    private SelectGroupAdapter adapter;
    private List<GroupContacts> listGroup = new ArrayList<>();
    private ContactViewModel viewModel;
    private LinearLayout layoutCreateNewGroup;


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
        ImageView imageViewCancelSelectGroup = view.findViewById(R.id.imageViewCancelSelectGroupForCreateContact);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new SelectGroupAdapter(view.getContext(), listGroup, s -> {
            viewModel.setsSelectGroup(s);
            getParentFragmentManager().popBackStack();
            return null;
        });
        recyclerView.setAdapter(adapter);

        LiveData<List<GroupContacts>> liveData = viewModel.getAllGroupContacts();
        liveData.observe(getViewLifecycleOwner(), groupContacts -> {
            if (groupContacts != null) {

                adapter.setList(groupContacts);
            }
        });

        layoutCreateNewGroup.setOnClickListener(view1 -> {
            new DialogFragmentContacts(viewModel, null, ActionEnum.CREATE_NEW_GROUP).show(getChildFragmentManager(), DialogFragmentContacts.TAG);
        });

        imageViewCancelSelectGroup.setOnClickListener(view1 -> {
            getParentFragmentManager().popBackStack();
        });


    }



}
