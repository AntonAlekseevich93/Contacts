package com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.db.entity.GroupContacts;
import com.example.hrcontact.db.entity.SubGroupContact;
import com.example.hrcontact.model.SubGroupOfSelectGroup;
import com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment.IListenerNestedSelectAdapter;
import com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment.ISelectAdapterListeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectGroupAdapter extends RecyclerView.Adapter<SelectGroupAdapter
        .SelectContactViewHolder> implements IListenerNestedSelectAdapter {
    private final Context context;
    private List<SubGroupOfSelectGroup> groupContactsList;
    private final List<GroupContacts> listOfGroup = new ArrayList<>();
    private List<SubGroupContact> listSubGroup = new ArrayList<>();
    private Map<Integer, String> mapOfSelectedGroup = new HashMap<>();
    private Map<Integer, String> mapOfSelectedSubGroup = new HashMap<>();
    private final ISelectAdapterListeners adapterListener;

    public SelectGroupAdapter(Context context, List<SubGroupOfSelectGroup> groupContactsList,
                              ISelectAdapterListeners adapterListener) {
        this.context = context;
        this.groupContactsList = groupContactsList;
        this.adapterListener = adapterListener;
        createListGroup(groupContactsList);
    }

    public void setList(List<SubGroupOfSelectGroup> groupContactsList) {
        this.groupContactsList = groupContactsList;
        createListGroup(groupContactsList);
        notifyDataSetChanged();
    }

    public void setAmountSelectedSubgroup(Map<Integer, Integer> map) {
        for (int i = 0; i < listOfGroup.size(); i++) {
            int key = listOfGroup.get(i).getId();
            if (map.containsKey(key)) {
                int amount = map.get(key);
                listOfGroup.get(i).setCounterSelectedSubGroup(amount);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Метод парсит список всех имеющихся групп. Где каждая группа содержит список подгрупп
     *
     * @param list<SubGroupOfSelectGroup> список Групп. В каждой группе имеется список
     *                                    подгрупп принадлежащих к этой группе.
     */
    private void createListGroup(List<SubGroupOfSelectGroup> list) {
        if (list != null) {
            listOfGroup.clear();
            for (SubGroupOfSelectGroup g : list) {
                listOfGroup.add(new GroupContacts(g.getId(), g.getNameGroup()));
            }
        }
    }

    public void setMapOfSelectedGroup(Map<Integer, String> mapOfSelectedGroup) {
        this.mapOfSelectedGroup = mapOfSelectedGroup;
        notifyDataSetChanged();
    }

    public void setMapOfSelectedSubGroup(Map<Integer, String> mapOfSelectedSubGroup) {
        this.mapOfSelectedSubGroup = mapOfSelectedSubGroup;
    }


    @NonNull
    @Override
    public SelectContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_group_with_nested_subgroup_for_select, parent, false);
        return new SelectContactViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectContactViewHolder holder, int position) {
        holder.tvNameGroup.setText(listOfGroup.get(position).getNameGroup());

        if (mapOfSelectedGroup.containsKey(listOfGroup.get(position).getId())) {
            listOfGroup.get(position).setSelect(true);
        }

        if (listOfGroup.get(position).isSelect()) {
            holder.tvInfoSelectGroup.setVisibility(View.VISIBLE);
        } else holder.tvInfoSelectGroup.setVisibility(View.GONE);

        boolean isExpandable = listOfGroup.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable) {
            holder.imgArrowOpenGroup.setImageDrawable(context.getDrawable(R.drawable.upp_arrow));
        } else
            holder.imgArrowOpenGroup
                    .setImageDrawable(context.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));

        if (listSubGroup != null)
            listSubGroup = groupContactsList.get(position).getListSubGroup();

        NestedSubGroupAdapter adapter = new NestedSubGroupAdapter(
                listSubGroup, position, this, mapOfSelectedSubGroup);
        holder.nestedRecyclerView
                .setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setAdapter(adapter);

        /**
         * Функция обрабатывает щелчек на кнопку показать подгруппы/скрыть подгруппы
         */
        holder.rvClickExpandableGroup.setOnClickListener(view -> {
            listOfGroup.get(position).setExpandable(!listOfGroup.get(position).isExpandable());
            if (listSubGroup != null)
                listSubGroup = groupContactsList.get(position).getListSubGroup();
            notifyItemChanged(holder.getAdapterPosition());
        });

        //Установка количества выбранных подгрупп
        if (listOfGroup.get(position).getCounterSelectedSubGroup() > 0) {
            holder.linearInfoAmountSelectedSubgroup.setVisibility(View.VISIBLE);
            String i = String.valueOf(listOfGroup.get(position).getCounterSelectedSubGroup());
            holder.tvAmountSelectedSubgroup.setText(i);
        } else holder.linearInfoAmountSelectedSubgroup.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return groupContactsList.size();
    }

    /**
     * Принимает данные выбранных подгрупп для передачи во фрагмент и сохранения выбранных подгрупп.
     *
     * @param idSubGroup id  подгруппы
     * @param name       имя  подгруппы
     * @param idGroup    id подгруппы
     */
    @Override
    public void setIdAndNameFromSelectedGroup(int idSubGroup, String name, int idGroup, int positionGroup) {
        if (!listOfGroup.get(positionGroup).isSelect()) {
            listOfGroup.get(positionGroup).setSelect(true);
            adapterListener
                    .setIdAndNameFromSelectedGroupOrSubgroup(idGroup,
                            listOfGroup.get(positionGroup).getNameGroup(), 0);
            SelectGroupAdapter.this.notifyItemChanged(positionGroup);
        }
        adapterListener.setIdAndNameFromSelectedGroupOrSubgroup(idSubGroup, name, 1);
    }


    /**
     * Метод устанавливает кол-во выбранных подгрупп для конкретной группы.
     *
     * @param position           позиция группы для которой увеличивается кол-во выбранных подгрупп.
     * @param increaseOrDecrease если true - кол-во выбранных подгрупп увеличивается.
     *                           Если false - уменьшается
     */
    @Override
    public void setNumberOfSelectedSubGroup(int position, boolean increaseOrDecrease) {
        listOfGroup.get(position).setCounterSelectedSubGroup(increaseOrDecrease);
        int idGroup = listOfGroup.get(position).getId();
        adapterListener.setCountSelectedGroup(idGroup, increaseOrDecrease);
        notifyItemChanged(position);
    }


    public class SelectContactViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNameGroup;
        final SelectGroupAdapter adapter;
        private final RelativeLayout expandableLayout;
        private final RecyclerView nestedRecyclerView;
        private final RelativeLayout rvClickExpandableGroup;
        private final TextView tvInfoSelectGroup;
        private final ImageView imgArrowOpenGroup;
        private final LinearLayout linearInfoAmountSelectedSubgroup;
        private final TextView tvAmountSelectedSubgroup;


        public SelectContactViewHolder(@NonNull View itemView, SelectGroupAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            tvNameGroup = itemView.findViewById(R.id.itemTv);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
            rvClickExpandableGroup = itemView.findViewById(R.id.relativeLayoutExpandableGroup);
            TextView tvCreateNewSubGroup = itemView.findViewById(R.id.tvCreateNewSubGroup);
            tvInfoSelectGroup = itemView.findViewById(R.id.tvParentAdapterGroupIsSelectInfo);
            imgArrowOpenGroup = itemView.findViewById(R.id.arro_imageview);
            linearInfoAmountSelectedSubgroup = itemView.findViewById(R.id.linearInfoAmountSelectedSubgroup);
            tvAmountSelectedSubgroup = itemView.findViewById(R.id.tvAmountSelectedSubgroup);


            /**
             * Метод обрабатывает нажатие на кнопку "Создать подгруппу"
             * передавая в вызывающий фрагмент ID GROUP для которой создается подгруппа
             */
            tvCreateNewSubGroup.setOnClickListener(view -> {
                adapterListener.createNewSubgroup(listOfGroup.get(getLayoutPosition()).getId());
            });

            /**
             * Метод обрабатывает установку и снятие выбора группы
             */
            tvNameGroup.setOnClickListener(view -> {
                if (!listOfGroup.get(getLayoutPosition()).isSelect())
                    listOfGroup.get(getLayoutPosition()).setSelect(true);
                else listOfGroup.get(getLayoutPosition()).setSelect(false);

                adapterListener.setIdAndNameFromSelectedGroupOrSubgroup(
                        listOfGroup.get(getLayoutPosition()).getId(),
                        listOfGroup.get(getLayoutPosition()).getNameGroup(), 0);

                notifyItemChanged(getLayoutPosition());
            });
        }
    }


}
