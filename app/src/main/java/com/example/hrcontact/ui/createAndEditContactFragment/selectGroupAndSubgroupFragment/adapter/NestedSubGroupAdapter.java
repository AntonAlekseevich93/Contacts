package com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.db.entity.SubGroupContact;
import com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment.IListenerNestedSelectAdapter;

import java.util.List;
import java.util.Map;

public class NestedSubGroupAdapter extends RecyclerView.Adapter<NestedSubGroupAdapter.NestedViewHolder> {
    private List<SubGroupContact> listSubGroup;
    private final Map<Integer, String> mapOfSelectedSubGroup;
    private final int positionGroup;
    private final IListenerNestedSelectAdapter iListenerNestedSelectAdapter;


    public NestedSubGroupAdapter(List<SubGroupContact> listSubGroup,
                                 int positionGroup,
                                 IListenerNestedSelectAdapter iListenerNestedSelectAdapter,
                                 Map<Integer, String> mapOfSelectedSubGroup) {
        if (listSubGroup != null)
            this.listSubGroup = listSubGroup;
        this.iListenerNestedSelectAdapter = iListenerNestedSelectAdapter;
        this.mapOfSelectedSubGroup = mapOfSelectedSubGroup;
        this.positionGroup = positionGroup;
    }


    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected_group, parent, false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        if (mapOfSelectedSubGroup != null
                && mapOfSelectedSubGroup.containsKey(listSubGroup.get(position).getId())) {
            listSubGroup.get(position).setSelect(true);
        }

        if (listSubGroup.get(position).isSelect()) {
            holder.tvInfoSelect.setVisibility(View.VISIBLE);
        } else holder.tvInfoSelect.setVisibility(View.GONE);

        holder.tv.setText(listSubGroup.get(position).getNameSubGroup());
    }

    @Override
    public int getItemCount() {
        return listSubGroup.size();
    }


    public class NestedViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;
        private final TextView tvInfoSelect;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.itemSelectedGroup);
            CardView setSelectGroup = itemView.findViewById(R.id.cardViewNestedAdapter);
            tvInfoSelect = itemView.findViewById(R.id.tvNestedAdapter);


            /**
             * Метод обрабатывает установку и снятие выбора подгруппы
             */
            setSelectGroup.setOnClickListener(view -> {
                int position = getLayoutPosition();
                if (!listSubGroup.get(position).isSelect())
                    listSubGroup.get(position).setSelect(true);
                else listSubGroup.get(position).setSelect(false);

                int groupIsThisSubgroup = listSubGroup.get(position).getIdGroup();

                iListenerNestedSelectAdapter.setIdAndNameFromSelectedGroup(
                        listSubGroup.get(position).getId(),
                        listSubGroup.get(position).getNameSubGroup(),
                        groupIsThisSubgroup, positionGroup);

                /**
                 * передается true если подгруппа выбрана - тогда будем увелчивать кол-во
                 * выбранных подгрупп. false - будем уменьшать кол-во выбранных подгрупп
                 */
                iListenerNestedSelectAdapter.setNumberOfSelectedSubGroup(positionGroup,
                        listSubGroup.get(position).isSelect());

                notifyItemChanged(position);
            });
        }
    }
}
