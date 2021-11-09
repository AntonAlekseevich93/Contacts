package com.example.contacts.screens.groupFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.screens.createContactFragment.CreateContactFragment;
import com.example.contacts.screens.contactFragment.ContactFragment;
import com.example.contacts.screens.dialogFragmentContact.DialogFragmentContacts;
import com.example.contacts.screens.groupFragment.adapter.GroupAdapter;
import com.example.contacts.screens.viewmodel.ContactViewModel;
import com.example.contacts.support.ActionEnum;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GroupFragment extends Fragment {
    private ContactViewModel viewModel;
    private RecyclerView recyclerView;
    private GroupAdapter adapter;
    private List<GroupContacts> listOfContact = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;
    private FloatingActionButton fabAddNewContact;
    public static final String PARAM_BUNDLE_KEY1 = "param1";
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
        adapter = new GroupAdapter(context, listOfContact, s -> {
            openGroupOfContact(s);
            return null;
        }, s -> {
            new DialogFragmentContacts(viewModel, s, ActionEnum.EDIT_GROUP).show(getChildFragmentManager(), DialogFragmentContacts.TAG);
            return null;
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_group_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabAddNewContact = view.findViewById(R.id.fabAddContact);
        tvNameToolbar = view.findViewById(R.id.tvNameToolbar);

        tvNameToolbar.setText(R.string.toolbar_name_group);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);


        LiveData<List<GroupContacts>> liveData = viewModel.getAllGroupContacts();
        liveData.observe(getViewLifecycleOwner(), groupContacts -> {
            if (groupContacts != null) adapter.setListOfContact(groupContacts);
        });

        fabAddNewContact.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, new CreateContactFragment())
                    .addToBackStack(null)
                    .commit();
        });




    }

    private void openGroupOfContact(String nameGroup) {
        ContactFragment contactFragment = new ContactFragment();
        Bundle arguments = new Bundle();
        arguments.putString(PARAM_BUNDLE_KEY1, nameGroup);
        contactFragment.setArguments(arguments);

        fragmentManager.beginTransaction()
                .replace(R.id.containerGroup, contactFragment)
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
