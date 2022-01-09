package com.example.hrcontact.support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.contacts.R;
import com.example.hrcontact.ui.DeleteFragment;
import com.example.hrcontact.ui.DialogFragmentContacts;
import com.example.hrcontact.ui.MainFragment.MainFragment;
import com.example.hrcontact.viewmodel.ContactViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetDialogGroup extends BottomSheetDialogFragment implements IClickListenerDismiss {
    private String name;
    private int type;
    private int id;
    private ContactViewModel contactViewModel;
    public IClickListenerDismiss iClickListenerDismiss;

    public static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogGroup();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString(MainFragment.PARAM_BUNDLE_KEY_NAME);
        type = getArguments().getInt(MainFragment.PARAM_BUNDLE_KEY_TYPE);
        id = getArguments().getInt(MainFragment.PARAM_BUNDLE_KEY_ID);
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editName(view);
        LinearLayout deleteGroupOrSubgroup = view.findViewById(R.id.deleteGroup);
        deleteGroupOrSubgroup.setOnClickListener(view1 -> {
            switch (type) {
                case 0:
                    startDeleteFragmentForDeleteGroupOrSubgroup(ActionEnum.DELETE_GROUP,
                            id, 1);
                    break;

                case 1:
                    startDeleteFragmentForDeleteGroupOrSubgroup(ActionEnum.DELETE_SUBGROUP,
                            id, 2);
                    break;
            }
        });
    }

    private void editName(View view) {
        LinearLayout editName = view.findViewById(R.id.editNameGroup);
        editName.setOnClickListener(view1 -> {
            switch (type) {
                case 0:
                    startDialogFragmentForEditName(name, ActionEnum.EDIT_GROUP);

                    break;

                case 1:
                    startDialogFragmentForEditName(name, ActionEnum.EDIT_SUB_GROUP);

                    break;
            }
        });
    }

    private void startDialogFragmentForEditName(String name, ActionEnum actionEnum) {
        DialogFragmentContacts dialogFragmentContacts = new DialogFragmentContacts();
        Bundle bundle = new Bundle();
        bundle.putString(DialogFragmentContacts.TAG_DIALOG_NAME_SELECTED_GROUP, name);
        bundle.putSerializable(DialogFragmentContacts.TAG_DIALOG_ACTION_ENUM, actionEnum);
        dialogFragmentContacts.setArguments(bundle);
        dialogFragmentContacts.show(getChildFragmentManager(), DialogFragmentContacts.TAG);
    }

    private void startDeleteFragmentForDeleteGroupOrSubgroup(ActionEnum actionEnum, int id, int type) {
        DeleteFragment deleteFragment = new DeleteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DeleteFragment.TAG_ACTION, actionEnum);
        bundle.putInt(DeleteFragment.TAG_ID, id);
        bundle.putInt(DeleteFragment.TAG_TYPE, type);
        deleteFragment.setArguments(bundle);
        deleteFragment.show(getChildFragmentManager(), DeleteFragment.TAG);
    }


    @Override
    public void startDismiss() {
        dismiss();
    }

    @Override
    public void startDismissWithUpdateData() {
        contactViewModel.getAllGroupContactsWithNestedSubGroupFromDB();
        dismiss();
    }


}
