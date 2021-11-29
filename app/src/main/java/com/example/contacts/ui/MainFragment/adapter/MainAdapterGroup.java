package com.example.contacts.ui.MainFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.db.entity.GroupContacts;
import com.example.contacts.db.entity.SubGroupContact;
import com.example.contacts.db.relation.SubGroupOfSelectGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import io.reactivex.functions.Function3;

public class MainAdapterGroup extends RecyclerView.Adapter<MainAdapterGroup.ContactViewHolder> {
    private List<GroupContacts> listOfGroup = new ArrayList<>();
    private List<SubGroupContact> listSubGroup = new ArrayList<>();
    private List<SubGroupOfSelectGroup> listSubGroupOfSelectedGroup = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private BiFunction<Integer, Integer, Void> clickListenerGroup;
    private Function3<String, Integer, Integer, Void> clickListenerEditGroup;
    private MainNestedAdapterSubGroup adapter;


    public MainAdapterGroup(Context context, List<GroupContacts> listOfGroup,
                            BiFunction<Integer, Integer, Void> clickListenerGroup,
                            Function3<String, Integer, Integer, Void> clickListenerEditGroup) {
        this.listOfGroup = listOfGroup;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.clickListenerGroup = clickListenerGroup;
        this.clickListenerEditGroup = clickListenerEditGroup;
    }

    public void setListGroupAndSubgroup(List<SubGroupOfSelectGroup> listSubGroupOfSelectedGroup) {
        this.listSubGroupOfSelectedGroup = listSubGroupOfSelectedGroup;
        createListGroup(this.listSubGroupOfSelectedGroup);
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

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_item_group_with_nested_subgroup, parent, false);
        return new ContactViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.tvNameGroup.setText(listOfGroup.get(position).getNameGroup());
        boolean isExpandable = listOfGroup.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (!isExpandable) {
            holder.clickExpandableGroup.setText(context.getResources().getString(R.string.show_subgroup));
        } else
            holder.clickExpandableGroup.setText(context.getResources().getString(R.string.hide_subgroup));
        if (listSubGroup != null)
            listSubGroup = listSubGroupOfSelectedGroup.get(position).getListSubGroup();

        adapter = new MainNestedAdapterSubGroup(listSubGroup,
                /**
                 * функция получает id подгруппы и возвращает в фрагмент для получения списка
                 * контактов данной подгруппы
                 */
                idSubGroup -> {
                    clickListenerGroup.apply(idSubGroup, 1);
                    return null;
                },
                /**
                 * Функция получает name подгруппы для редактирования названия подгруппы
                 * Вторым параметром мы передаем во фрагмент тип подгруппы - 1
                 * Третий параметр id подгруппы
                 */
                (name, id) -> {
                    try {
                        clickListenerEditGroup.apply(name, 1, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setAdapter(adapter);
        /**
         * Функция обрабатывает щелчек на кнопку показать подгруппы/скрыть подгруппы
         */
        holder.clickExpandableGroup.setOnClickListener(view -> {
            listOfGroup.get(position).setExpandable(!listOfGroup.get(position).isExpandable());
            notifyItemChanged(holder.getAdapterPosition());
        });
    }


    @Override
    public int getItemCount() {
        if (listOfGroup != null) {
            return listOfGroup.size();
        } else return 0;
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        final MainAdapterGroup adapter;
        final TextView tvNameGroup;
        private RelativeLayout expandableLayout;
        private RecyclerView nestedRecyclerView;
        private TextView clickExpandableGroup;
        public final ImageView imageViewClickEditNameGroup;

        public ContactViewHolder(@NonNull View itemView, MainAdapterGroup adapter) {
            super(itemView);
            this.adapter = adapter;
            tvNameGroup = itemView.findViewById(R.id.main_itemTv);
            expandableLayout = itemView.findViewById(R.id.main_expandable_layout);
            nestedRecyclerView = itemView.findViewById(R.id.main_child_rv);
            clickExpandableGroup = itemView.findViewById(R.id.tv_show_subgroup);
            imageViewClickEditNameGroup = itemView.findViewById(R.id.main_edit_group_imageview);
            /**
             * ClickListener получает id группы и возвращает в фрагмент для получения списка
             * контактов данной группы
             */
            tvNameGroup.setOnClickListener(v -> {
                int idGroup = listOfGroup.get(getLayoutPosition()).getId();
                clickListenerGroup.apply(idGroup, 0);
            });
            imageViewClickEditNameGroup.setOnClickListener(v -> {
                try {
                    clickListenerEditGroup.apply(tvNameGroup.getText().toString(), 0, listOfGroup.get(getLayoutPosition()).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


        }


    }
}