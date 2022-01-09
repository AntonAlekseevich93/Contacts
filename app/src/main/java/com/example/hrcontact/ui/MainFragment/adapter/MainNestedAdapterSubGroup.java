package com.example.hrcontact.ui.MainFragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.db.entity.SubGroupContact;
import com.example.hrcontact.ui.MainFragment.IMainAdapterListener;

import java.util.ArrayList;
import java.util.List;

public class MainNestedAdapterSubGroup extends
        RecyclerView.Adapter<MainNestedAdapterSubGroup.MainNestedSubGroupViewHolder> {

    private List<SubGroupContact> listSubGroup = new ArrayList<>();
    private final IMainAdapterListener iMainAdapterListener;


    public MainNestedAdapterSubGroup(List<SubGroupContact> listSubGroup,
                                     IMainAdapterListener iMainAdapterListener) {
        if (listSubGroup != null) {
            this.listSubGroup = listSubGroup;
        }
        this.iMainAdapterListener = iMainAdapterListener;
    }

    @NonNull
    @Override
    public MainNestedSubGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_nested_adapter_item, parent, false);
        return new MainNestedSubGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainNestedSubGroupViewHolder holder, int position) {
        holder.tvNameSubGroup.setText(listSubGroup.get(position).getNameSubGroup());
    }

    @Override
    public int getItemCount() {
        return listSubGroup.size();
    }

    class MainNestedSubGroupViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNameSubGroup;

        public MainNestedSubGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSubGroup = itemView.findViewById(R.id.tvNameSubgroupMainNestedAdapter);
            ImageView imageViewEditNameSubGroup = itemView.findViewById(R.id.main_edit_SubGroup_imageview);

            /**
             * ClickListener получает id подгруппы и возвращает в фрагмент для получения списка
             * контактов данной подгруппы
             */
            tvNameSubGroup.setOnClickListener(v -> {
                int idSubGroup = listSubGroup.get(getLayoutPosition()).getId();
                iMainAdapterListener.openContactsForThisGroupOrSubgroup(idSubGroup, 1);
            });


            imageViewEditNameSubGroup.setOnClickListener(v -> {
                int position = getLayoutPosition();
                int idSubgroup = listSubGroup.get(position).getId();
                String nameSubGroup = listSubGroup.get(position).getNameSubGroup();
                iMainAdapterListener
                        .editNameGroupOrSubgroup(nameSubGroup, 1, idSubgroup);
            });

        }
    }
}
