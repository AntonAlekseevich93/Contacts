package com.example.hrcontact.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.contacts.R;
import com.example.hrcontact.support.IClickListenerDismiss;
import com.example.hrcontact.viewmodel.ContactViewModel;
import com.example.hrcontact.support.ActionEnum;

/**
 * Класс выводит диалог фрагмент, который принимает в себя события: Создать новую группу,
 * создать новую подгруппу, изменить название группы, изменить название подгруппы
 **/
public class DialogFragmentContacts extends DialogFragment {
    public static String TAG = "CreateNewDialogFragment";
    private ContactViewModel contactViewModel;
    private AlertDialog alertDialog;
    private EditText editText;
    private Context context;
    private String nameSelectedGroup;
    private ActionEnum actionEnum;
    private int nameTitleDialog;
    private int idGroup;
    public static final String TAG_DIALOG_NAME_SELECTED_GROUP = "tagNameSelectedGroup";
    public static final String TAG_DIALOG_ACTION_ENUM = "tagActionEnum";
    public static final String TAG_DIALOG_ID_GROUP = "tagIdGroup";
    private IClickListenerDismiss iClickListenerDismiss;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (requireParentFragment() instanceof IClickListenerDismiss)
            iClickListenerDismiss = (IClickListenerDismiss) requireParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        nameSelectedGroup = getArguments().getString(TAG_DIALOG_NAME_SELECTED_GROUP);
        actionEnum = (ActionEnum) getArguments().getSerializable(TAG_DIALOG_ACTION_ENUM);
        idGroup = getArguments().getInt(TAG_DIALOG_ID_GROUP);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment_create_new_group, null);

        editText = view.findViewById(R.id.editTextDialogFragmentNewGroup);

        switch (actionEnum) {
            case CREATE_NEW_GROUP:
                nameTitleDialog = R.string.dialogFragmentNameCreateGroup;
                break;
            case CREATE_NEW_SUB_GROUP:
                nameTitleDialog = R.string.dialogFragmentNameCreateSubGroup;
                break;
            case EDIT_GROUP:
                nameTitleDialog = R.string.dialogFragmentEditGroup;
                editText.setText(nameSelectedGroup);
                break;
            case EDIT_SUB_GROUP:
                nameTitleDialog = R.string.dialogFragmentEditSubGroup;
                editText.setText(nameSelectedGroup);
        }

        alertDialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setMessage(getString(nameTitleDialog))
                .setNegativeButton(getString(R.string.negativeButtonDialogFragment), (((dialogInterface, i) -> {
                    getChildFragmentManager().popBackStack();
                })))
                .setPositiveButton(getString(R.string.positiveButtonDialogFragment), ((dialogInterface, i) -> {
                    switch (actionEnum) {

                        case CREATE_NEW_GROUP:
                            String sNewGroup = editText.getText().toString();
                            LiveData<Boolean> liveData = contactViewModel.createNewGroup(sNewGroup);
                            liveData.observe(requireActivity(), aBoolean -> {
                                if (aBoolean) {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_group_added),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                } else {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_group_already_exist),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                }
                            });
                            break;

                        case CREATE_NEW_SUB_GROUP:
                            String nameSubGroup = editText.getText().toString();

                            LiveData<Boolean> liveData2 =
                                    contactViewModel.createNewSubGroup(idGroup, nameSubGroup);
                            liveData2.observe(requireActivity(), aBoolean -> {
                                if (aBoolean) {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_subgroup_added),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                } else {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_subgroup_already_exist),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                }
                            });
                            break;

                        case EDIT_GROUP:
                            String newNameGroup = editText.getText().toString();
                            LiveData<Boolean> liveData3 = contactViewModel
                                    .editNameGroupOrSubgroup(nameSelectedGroup, newNameGroup, 0);
                            liveData3.observe(requireActivity(), aBoolean -> {
                                if (aBoolean) {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_nameGroup_changed),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                    iClickListenerDismiss.startDismiss();
                                } else {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_group_already_exist),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                }
                            });

                            break;

                        case EDIT_SUB_GROUP:
                            String newNameSubGroup = editText.getText().toString();
                            LiveData<Boolean> liveData4 = contactViewModel
                                    .editNameGroupOrSubgroup(nameSelectedGroup, newNameSubGroup, 1);
                            liveData4.observe(requireActivity(), aBoolean -> {
                                if (aBoolean) {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_nameSubgroup_changed),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                    iClickListenerDismiss.startDismiss();
                                } else {
                                    Toast.makeText(context,
                                            context.getResources().getString(
                                                    R.string.toast_info_subgroup_already_exist),
                                            Toast.LENGTH_SHORT).show();
                                    contactViewModel.setBooleanLiveDataNull();
                                }
                            });
                            break;
                    }


                }))
                .create();
        return alertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });
    }


}

