package com.example.contacts.ui.createContactFragment.selectGroupAndSubgroupFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.db.relation.SubGroupOfSelectGroup;
import com.example.contacts.db.entity.GroupContacts;
import com.example.contacts.db.entity.SubGroupContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.reactivex.functions.Function3;

public class SelectGroupAdapter extends RecyclerView.Adapter<SelectGroupAdapter.SelectContactViewHolder> {
    private Context context;
    private List<SubGroupOfSelectGroup> groupContactsList;
    private Function3<Integer, String, Integer, Void> functionReturnIdAndNameFromSelectedGroupOrSubgroup;
    private List<GroupContacts> listOfGroup = new ArrayList<>();
    private List<SubGroupContact> listSubGroup = new ArrayList<>();
    private Function<Integer, Void> listenerSelectGroupForCreateNewSubGroup;
    private Map<Integer, String> mapOfSelectedGroup = new HashMap<>();
    private Map<Integer, String> mapOfSelectedSubGroup = new HashMap<>();
    private NestedSubGroupAdapter adapter;


    public SelectGroupAdapter(Context context, List<SubGroupOfSelectGroup> groupContactsList,
                              Function3<Integer, String, Integer, Void> functionReturnIdAndNameFromSelectedGroupOrSubgroup,
                              Function<Integer, Void> listenerSelectGroupForCreateNewSubGroup) {
        this.context = context;
        this.groupContactsList = groupContactsList;
        createListGroup(groupContactsList);
        this.functionReturnIdAndNameFromSelectedGroupOrSubgroup = functionReturnIdAndNameFromSelectedGroupOrSubgroup;
        this.listenerSelectGroupForCreateNewSubGroup = listenerSelectGroupForCreateNewSubGroup;


    }

    public void setList(List<SubGroupOfSelectGroup> groupContactsList) {
        this.groupContactsList = groupContactsList;
        createListGroup(groupContactsList);
        notifyDataSetChanged();
    }

    /**
     * Метод парсит список всех имеющихся групп. Где каждая группа содержит список подгрупп
     * @param list<SubGroupOfSelectGroup> список Групп. В каждой группе имеется список
     *                                   подгрупп принадлежащих к этой группе.
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_with_nested_subgroup_for_select, parent, false);
        return new SelectContactViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectContactViewHolder holder, int position) {

        if (mapOfSelectedGroup.containsKey(listOfGroup.get(position).getId())) {
            listOfGroup.get(position).setSelect(true);

        }

        if (listOfGroup.get(position).isSelect()) {
            holder.tvInfoSelectGroup.setVisibility(View.VISIBLE);
        } else holder.tvInfoSelectGroup.setVisibility(View.GONE);


        holder.tvNameGroup.setText(listOfGroup.get(position).getNameGroup());

        boolean isExpandable = listOfGroup.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        if (isExpandable) {
            //Изменяем картинку открытой стрелочки и закрытой
        } else ;
        if (listSubGroup != null)
            listSubGroup = groupContactsList.get(position).getListSubGroup();


        adapter = new NestedSubGroupAdapter(listSubGroup, new BiFunction<Integer, String, Void>() {
            @Override
            public Void apply(Integer integer, String s) {
                try {
                    functionReturnIdAndNameFromSelectedGroupOrSubgroup.apply(integer, s, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, mapOfSelectedSubGroup);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
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

    }

    @Override
    public int getItemCount() {

        return groupContactsList.size();

    }


    public class SelectContactViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNameGroup;
        //        final RelativeLayout relativeLayoutClickAtItem;
        final SelectGroupAdapter adapter;
        //        final LinearLayout linearLayoutForSetBackground;
        private RelativeLayout expandableLayout;
        private RecyclerView nestedRecyclerView;
        private RelativeLayout rvClickExpandableGroup;
        private RelativeLayout relativeLayoutCreateNewSubGroup;
        private LinearLayout clickSelectGroup;
        private RelativeLayout setBackgroundColorSelectedGroup;
        private TextView tvInfoSelectGroup;


        public SelectContactViewHolder(@NonNull View itemView, SelectGroupAdapter adapter) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            tvNameGroup = itemView.findViewById(R.id.itemTv);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
            rvClickExpandableGroup = itemView.findViewById(R.id.relativeLayoutExpandableGroup);
            relativeLayoutCreateNewSubGroup = itemView.findViewById(R.id.relativeCreateNewSubGroup);
            clickSelectGroup = itemView.findViewById(R.id.linear_layout_of_parent_adapter);
            setBackgroundColorSelectedGroup = itemView.findViewById(R.id.relativeLayoutOfParentAdapter);
            tvInfoSelectGroup = itemView.findViewById(R.id.tvParentAdapterGroupIsSelectInfo);

            /**
             * Метод обрабатывает нажатие на кнопку "Создать подгруппу"
             * передавая в вызывающий фрагмент ID GROUP для которой создается подгруппа
             */
            relativeLayoutCreateNewSubGroup.setOnClickListener(view -> {
                listenerSelectGroupForCreateNewSubGroup.apply(listOfGroup.get(getLayoutPosition()).getId());
            });

            /**
             * Метод обрабатывает установку и снятие выбора группы
             */
            tvNameGroup.setOnClickListener(view -> {
                if (!listOfGroup.get(getLayoutPosition()).isSelect())
                    listOfGroup.get(getLayoutPosition()).setSelect(true);

                else listOfGroup.get(getLayoutPosition()).setSelect(false);

                try {
                    functionReturnIdAndNameFromSelectedGroupOrSubgroup.apply(listOfGroup.get(getLayoutPosition()).getId(),
                            listOfGroup.get(getLayoutPosition()).getNameGroup(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyItemChanged(getLayoutPosition());
            });
            this.adapter = adapter;
        }
    }


}
