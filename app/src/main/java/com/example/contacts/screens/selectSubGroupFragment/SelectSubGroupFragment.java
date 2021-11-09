package com.example.contacts.screens.selectSubGroupFragment;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;
import com.example.contacts.screens.createContactFragment.CreateContactFragment;
import com.example.contacts.screens.dialogFragmentContact.DialogFragmentContacts;
import com.example.contacts.screens.selectGroupFragment.adapter.SelectGroupAdapter;
import com.example.contacts.screens.selectSubGroupFragment.adapter.SelectedSubGroupAdapter;
import com.example.contacts.screens.viewmodel.ContactViewModel;
import com.example.contacts.support.ActionEnum;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

public class SelectSubGroupFragment extends Fragment {
    private RecyclerView recyclerView;
    private SelectedSubGroupAdapter adapter;
    private List<SubGroupContact> listSubGroup = new ArrayList<>();
    private ContactViewModel viewModel;
    private String nameSelectedGroup;
    private LinearLayout linearLayoutCreateSUBGroup;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        nameSelectedGroup = getArguments().getString(CreateContactFragment.ARGUMENT_NAME_SELECTED_GROUP);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_select_sub_group_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewSelectSUBGroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ImageView imageViewCancelSelectGroup = view.findViewById(R.id.imageViewCancelSelectSUBGroupForCreateContact);
        linearLayoutCreateSUBGroup = view.findViewById(R.id.linearLayoutCreateSUBGroup);
        adapter = new SelectedSubGroupAdapter(view.getContext(), listSubGroup, s -> {
            viewModel.setsSelectSubGroup(s);
            getParentFragmentManager().popBackStack();
            return null;
        });
        recyclerView.setAdapter(adapter);

        LiveData<List<SubGroupContact>> liveData = viewModel.getAllSubGroup(nameSelectedGroup);
        liveData.observe(getViewLifecycleOwner(), new Observer<List<SubGroupContact>>() {
            @Override
            public void onChanged(List<SubGroupContact> subGroupContacts) {
                if (subGroupContacts != null) {
                    adapter.setList(subGroupContacts);

                } else Toast.makeText(view.getContext(), "Такая подгруппа уже есть", Toast.LENGTH_SHORT).show();
            }
        });

        linearLayoutCreateSUBGroup.setOnClickListener(v ->{
            new DialogFragmentContacts(viewModel, nameSelectedGroup, ActionEnum.CREATE_NEW_SUB_GROUP).show(getChildFragmentManager(), DialogFragmentContacts.TAG);
        });

        imageViewCancelSelectGroup.setOnClickListener(view1 -> {
            getParentFragmentManager().popBackStack();
        });
    }

//    public static class CreateNewGroupDialogFragment extends DialogFragment {
//        public static String TAG = "CreateNewSUBGroupDialogFragment";
//        private ContactViewModel viewModel;
//        private AlertDialog alertDialog;
//        EditText editTextNewSubGroupCreate;
//        private Context context;
//        private String nameSelectedGroup;
//
//        @Override
//        public void onAttach(@NonNull Context context) {
//            super.onAttach(context);
//            this.context = context;
//        }
//
//        public CreateNewGroupDialogFragment(ContactViewModel viewModel, String nameSelectedGroup) {
//            this.viewModel = viewModel;
//            this.nameSelectedGroup = nameSelectedGroup;
//        }
//
//        @NonNull
//        @Override
//        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//            LayoutInflater inflater = requireActivity().getLayoutInflater();
//
//            View view = inflater.inflate(R.layout.dialog_fragment_create_new_group, null);
//
//            editTextNewSubGroupCreate = view.findViewById(R.id.editTextDialogFragmentNewGroup);
//
//            alertDialog = new AlertDialog.Builder(requireContext())
//                    .setView(view)
//                    .setMessage(getString(R.string.dialogFragmentNameCreateGroup))
//                    .setNegativeButton(getString(R.string.negativeButtonDialogFragment), (((dialogInterface, i) -> {
//                        getChildFragmentManager().popBackStack();
//                    })))
//                    .setPositiveButton(getString(R.string.positiveButtonDialogFragment), ((dialogInterface, i) -> {
//                        String nameSubGroup = editTextNewSubGroupCreate.getText().toString();
//
//                        LiveData<Boolean> liveData = viewModel.createNewSubGroup(nameSelectedGroup, nameSubGroup);
//                        liveData.observe(requireActivity(), aBoolean -> {
//                            if (aBoolean)
//                                Toast.makeText(context, "Подгруппа добавлена", Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(context, "Такая подгруппа уже существует", Toast.LENGTH_SHORT).show();
//                        });
//
//                    }))
//                    .create();
//            return alertDialog;
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
//            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//            editTextNewSubGroupCreate.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    if (editable.length() >= 1) {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                    } else {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                    }
//                }
//            });
//        }
//    }
}
