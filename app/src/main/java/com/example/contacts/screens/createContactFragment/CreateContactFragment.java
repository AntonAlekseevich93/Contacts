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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.screens.createContactFragment.selectGroupAndSubgroupFragment.SelectGroupAndSubgroupFragment;
import com.example.contacts.screens.viewmodel.ContactViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateContactFragment extends Fragment {
    private ContactViewModel viewModel;
    private TextView tvSelectGroup;

    private EditText edtNameContact;
    private EditText edtNumberContact;
    private Spinner spinnerPriorityContact;
    private EditText edtDescriptionContact;
    private ImageView imageViewSaveContact;
    private FragmentManager fragmentManager;
    public static final String ARGUMENT_NAME_SELECTED_GROUP = "ARGUMENT_NAME_SELECTED_GROUP";
    private RecyclerView recyclerViewGroup;
//    private RecyclerView recyclerViewSubGroup;
    private AdapterCreateContactFragment adapterGroup;
//    private AdapterCreateSubGroupContactFragment adapterSubGroup;
    private Map<Integer, String> mapGroup = new HashMap<>();
    private Map<Integer, String> mapSubGroup = new HashMap<>();


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
//        viewModel.clearMapGroup();
//        viewModel.clearMapSubGroup();

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
        viewModel.getAllGroupContactsWithNestedSubGroupFromDB();//получаем группы и подгруппы для выбора
        recyclerViewGroup = view.findViewById(R.id.recyclerViewSelectGroupForCreateContact);
//        recyclerViewSubGroup = view.findViewById(R.id.recyclerViewSelectSubGroupForCreateContact);
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerViewSubGroup.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterGroup = new AdapterCreateContactFragment(view.getContext(), mapGroup);
        //Удалить адаптер
//        adapterSubGroup = new AdapterCreateSubGroupContactFragment(view.getContext(), mapSubGroup);
        recyclerViewGroup.setAdapter(adapterGroup);
//        recyclerViewSubGroup.setAdapter(adapterSubGroup);

        if (viewModel.getsSelectGroup().length() > 0) {
            tvSelectGroup.setText(viewModel.getsSelectGroup());

        }



        tvSelectGroup.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.containerGroup, new SelectGroupAndSubgroupFragment())
                    .addToBackStack(null)
                    .commit();

        });

        //УЖЕ НЕ НУЖНО
//        tvSelectSubGroup.setOnClickListener(v -> {
//            String nameSelectedGroup = tvSelectGroup.getText().toString();
//            /**
//             * Переделать проверку
//             */
//            if (!nameSelectedGroup.equals("Без ")) {
//                SelectSubGroupFragment selectSubGroupFragment = new SelectSubGroupFragment();
//                Bundle arg = new Bundle();
//                arg.putString(ARGUMENT_NAME_SELECTED_GROUP, tvSelectGroup.getText().toString());
//                selectSubGroupFragment.setArguments(arg);
//                fragmentManager.beginTransaction()
//                        .replace(R.id.containerGroup, selectSubGroupFragment)
//                        .addToBackStack(null)
//                        .commit();
//            } else
//                Toast.makeText(view.getContext(), "Вначале нужно выбрать группу", Toast.LENGTH_SHORT).show();
//        });

        imageViewSaveContact.setOnClickListener(v -> {
            addNewContact();
        });


//        LiveData<Map<Integer, String>> getMapOfSelectedGroup = viewModel.getMapOfSelectedGroup();
//        getMapOfSelectedGroup.observe(getViewLifecycleOwner(), integerStringMap -> {
//            if (integerStringMap != null) {
//                adapterGroup.setMap(integerStringMap);
//            }
//        });
//
//        LiveData<Map<Integer, String>> getMapOfSelectedSubGroup = viewModel.getMapOfSelectedSubGroup();
//        getMapOfSelectedSubGroup.observe(getViewLifecycleOwner(), integerStringMap -> {
//            if (integerStringMap != null) {
//
////                adapterSubGroup.setMap(integerStringMap);
//            }
//        });

        /**
         * Функция получает имена выбранных групп и подгрупп и устанавливает их в адаптер
         */
        LiveData<List<String>> selectedGroupAndSubgroup = viewModel.getNameOfSelectedGroupAndSubgroup();
        selectedGroupAndSubgroup.observe(getViewLifecycleOwner(), s -> {
            adapterGroup.setNameSelectedGroupOrSubgroup(s);
        });
    }

    private void initializeViews(View view) {
        tvSelectGroup = view.findViewById(R.id.tvSelectGroup);

        edtNameContact = view.findViewById(R.id.editTextTextPersonName);
        edtNumberContact = view.findViewById(R.id.editTextTextPersonName2);
        spinnerPriorityContact = view.findViewById(R.id.spinnerSelectPriorityContact);
        edtDescriptionContact = view.findViewById(R.id.editTextAddDescriptionContact);
        imageViewSaveContact = view.findViewById(R.id.imageViewSaveContact);
    }
//
    private void addNewContact() {
        String nameContact = edtNameContact.getText().toString();
        String numberContact = edtNumberContact.getText().toString();
        int priority = spinnerPriorityContact.getSelectedItemPosition();
        String description = edtDescriptionContact.getText().toString();
        if (nameContact.length() <= 0 || numberContact.length() <= 0) {
            Toast.makeText(getContext(), "Введите данные", Toast.LENGTH_SHORT).show();
        } else {
            LiveData<Boolean> liveData = viewModel.createNewContact(nameContact, numberContact, priority, description);
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
