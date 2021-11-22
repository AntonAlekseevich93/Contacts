package com.example.contacts.screens.dialogFragmentContact;

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

import com.example.contacts.R;
import com.example.contacts.screens.MainActivity;
import com.example.contacts.screens.viewmodel.ContactViewModel;
import com.example.contacts.support.ActionEnum;

/**
 * Класс выводит диалог фрагмент, который принимает в себя события: Создать новую группу,
 * создать новую подгруппу, изменить название группы, изменить название подгруппы
 **/
public class DialogFragmentContacts extends DialogFragment {
    public static String TAG = "CreateNewDialogFragment";
    private ContactViewModel viewModel;
    private AlertDialog alertDialog;
    EditText editText;
    private Context context;
    private String nameSelectedGroup;
    private ActionEnum actionEnum;
    private int nameTitleDialog;
    private int idGroup;
    private int position;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DialogFragmentContacts(ContactViewModel viewModel, String nameSelectedGroup,int position,  int idGroup, ActionEnum actionEnum) {
        this.viewModel = viewModel;
        this.nameSelectedGroup = nameSelectedGroup;
        this.actionEnum = actionEnum;
        this.idGroup = idGroup;
        this.position = position;
    }

    public DialogFragmentContacts(ContactViewModel viewModel,  ActionEnum actionEnum) {
        this.viewModel = viewModel;
        this.nameSelectedGroup = nameSelectedGroup;
        this.actionEnum = actionEnum;
        this.idGroup = idGroup;
        this.position = position;
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
                            LiveData<Boolean> liveData = viewModel.createNewGroup(sNewGroup);
                            liveData.observe(requireActivity(), aBoolean -> {
                                if (aBoolean)
                                    Toast.makeText(context, "Группа добавлена", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context, "Такая группа уже существует", Toast.LENGTH_SHORT).show();
                            });
                            break;

                        case CREATE_NEW_SUB_GROUP:
                            String nameSubGroup = editText.getText().toString();

                            LiveData<Boolean> liveData2 = viewModel.createNewSubGroup(idGroup, nameSubGroup);
                            liveData2.observe(requireActivity(), aBoolean -> {
                                if (aBoolean)
                                    Toast.makeText(context, "Подгруппа добавлена", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context, "Такая подгруппа уже существует", Toast.LENGTH_SHORT).show();
                            });
                            break;

                        case EDIT_GROUP:
                            String newNameGroup = editText.getText().toString();
                            LiveData<Boolean> liveData3 = viewModel.editNameGroupOrSubgroup(nameSelectedGroup, newNameGroup, 0);
                            liveData3.observe(requireActivity(), aBoolean -> {
                                if (aBoolean) {
                                    Toast.makeText(context, "Название группы изменено", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(context, "Такая группа уже существует", Toast.LENGTH_SHORT).show();
                            });

                            break;

                        case EDIT_SUB_GROUP:

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

