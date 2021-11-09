package com.example.contacts.screens.createContactFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.contacts.R;
import com.example.contacts.screens.selectGroupFragment.SelectGroupFragment;
import com.example.contacts.screens.selectSubGroupFragment.SelectSubGroupFragment;
import com.example.contacts.screens.viewmodel.ContactViewModel;

import io.reactivex.Single;

public class CreateContactFragment extends Fragment {
    private ContactViewModel viewModel;
    private TextView tvSelectGroup;
    private TextView tvSelectSubGroup;
    private EditText edtNameContact;
    private EditText edtNumberContact;
    private Spinner spinnerPriorityContact;
    private EditText edtDescriptionContact;
    private ImageView imageViewSaveContact;
    private FragmentManager fragmentManager;
    public static final String ARGUMENT_NAME_SELECTED_GROUP = "ARGUMENT_NAME_SELECTED_GROUP";


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        fragmentManager = getParentFragmentManager();
        viewModel.setsSelectGroup("");
        viewModel.setsSelectSubGroup("");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_create_contact_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);

        if (viewModel.getsSelectGroup().length() > 0) {
            tvSelectGroup.setText(viewModel.getsSelectGroup());
//            viewModel.setsSelectGroup("");
        }

        if(viewModel.getsSelectSubGroup().length() >0 ){
            tvSelectSubGroup.setText(viewModel.getsSelectSubGroup());
//            viewModel.setsSelectSubGroup("");
        }

        tvSelectGroup.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, new SelectGroupFragment())
                    .addToBackStack(null)
                    .commit();
        });

        tvSelectSubGroup.setOnClickListener(v -> {
            String nameSelectedGroup = tvSelectGroup.getText().toString();
            if (!nameSelectedGroup.equals("Без группы")) {
                SelectSubGroupFragment selectSubGroupFragment = new SelectSubGroupFragment();
                Bundle arg = new Bundle();
                arg.putString(ARGUMENT_NAME_SELECTED_GROUP, tvSelectGroup.getText().toString());
                selectSubGroupFragment.setArguments(arg);
                fragmentManager.beginTransaction()
                        .replace(R.id.containerGroup, selectSubGroupFragment)
                        .addToBackStack(null)
                        .commit();
            } else
                Toast.makeText(view.getContext(), "Вначале нужно выбрать группу", Toast.LENGTH_SHORT).show();
        });

        imageViewSaveContact.setOnClickListener(v -> {
            addNewContact();
        });
    }

    private void initializeViews(View view) {
        tvSelectGroup = view.findViewById(R.id.tvSelectGroup);
        tvSelectSubGroup = view.findViewById(R.id.tvSelectSubGroup);
        edtNameContact = view.findViewById(R.id.editTextTextPersonName);
        edtNumberContact = view.findViewById(R.id.editTextTextPersonName2);
        spinnerPriorityContact = view.findViewById(R.id.spinnerSelectPriorityContact);
        edtDescriptionContact = view.findViewById(R.id.editTextAddDescriptionContact);
        imageViewSaveContact = view.findViewById(R.id.imageViewSaveContact);
    }

    private void addNewContact() {
        String selectGroup = tvSelectGroup.getText().toString();
        String selectSubGroup = tvSelectSubGroup.getText().toString();
        String nameContact = edtNameContact.getText().toString();
        String numberContact = edtNumberContact.getText().toString();
        int priority = spinnerPriorityContact.getSelectedItemPosition();
        String description = edtDescriptionContact.getText().toString();

        if (nameContact.length() <= 0 || numberContact.length() <= 0) {
            Toast.makeText(getContext(), "Введите данные", Toast.LENGTH_SHORT).show();
        } else {
            LiveData<Boolean> liveData = viewModel.createNewContact(selectGroup, selectSubGroup, nameContact, numberContact, priority, description);
            liveData.observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    Toast.makeText(getContext(), "Контакт добавлен", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else
                    Toast.makeText(getContext(), "Такой номер уже имеется", Toast.LENGTH_SHORT).show();
            });
        }
    }

}
