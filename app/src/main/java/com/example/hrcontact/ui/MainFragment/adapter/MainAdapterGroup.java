package com.example.hrcontact.ui.MainFragment.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.db.entity.GroupContacts;
import com.example.hrcontact.db.entity.SubGroupContact;
import com.example.hrcontact.model.SubGroupOfSelectGroup;
import com.example.hrcontact.ui.MainFragment.IMainAdapterListener;

import java.util.ArrayList;
import java.util.List;

public class MainAdapterGroup extends RecyclerView.Adapter<MainAdapterGroup.ContactViewHolder>
        implements IMainAdapterListener {

    private final List<GroupContacts> listOfGroup;
    private List<SubGroupContact> listSubGroup = new ArrayList<>();
    private List<SubGroupOfSelectGroup> listSubGroupOfSelectedGroup = new ArrayList<>();
    private final Context context;
    private final LayoutInflater inflater;
    private final IMainAdapterListener iMainAdapterListener;


    public MainAdapterGroup(Context context,
                            List<GroupContacts> listOfGroup,
                            IMainAdapterListener iMainAdapterListener) {
        this.listOfGroup = listOfGroup;
        this.context = context;
        this.iMainAdapterListener = iMainAdapterListener;
        inflater = LayoutInflater.from(context);
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

        //Если не группа для контактов без групп
        if (listOfGroup.get(position).getId() != 1) {
            boolean isExpandable = listOfGroup.get(position).isExpandable();
            holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

            if (!isExpandable) {
                holder.clickExpandableGroup.setText(context.getResources().getString(R.string.show_subgroup));
            } else
                holder.clickExpandableGroup.setText(context.getResources().getString(R.string.hide_subgroup));
            if (listSubGroup != null)
                listSubGroup = listSubGroupOfSelectedGroup.get(position).getListSubGroup();


            MainNestedAdapterSubGroup adapter =
                    new MainNestedAdapterSubGroup(listSubGroup, this);
            holder.nestedRecyclerView
                    .setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.nestedRecyclerView.setAdapter(adapter);

            /**
             * Функция обрабатывает щелчек на кнопку показать подгруппы/скрыть подгруппы
             */
            holder.clickExpandableGroup.setOnClickListener(view -> {
                listOfGroup.get(position).setExpandable(!listOfGroup.get(position).isExpandable());
                notifyItemChanged(holder.getAdapterPosition());
            });
        } else {
            holder.imageViewClickEditNameGroup.setVisibility(View.GONE);
            holder.clickExpandableGroup.setVisibility(View.GONE);
//            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorApp));
            holder.cardView.setMinimumHeight(30);
        }
    }


    @Override
    public int getItemCount() {
        if (listOfGroup != null) {
            return listOfGroup.size();
        } else return 0;
    }


    /**
     * функция получает id подгруппы и возвращает в фрагмент для получения списка
     * контактов данной подгруппы
     */
    @Override
    public void openContactsForThisGroupOrSubgroup(int id, int type) {
        iMainAdapterListener.openContactsForThisGroupOrSubgroup(id, type);
    }

    /**
     * Функция получает name подгруппы для редактирования названия подгруппы
     * Вторым параметром мы передаем во фрагмент тип подгруппы - 1
     * Третий параметр id подгруппы
     */
    @Override
    public void editNameGroupOrSubgroup(String name, int type, int id) {
        iMainAdapterListener.editNameGroupOrSubgroup(name, type, id);
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        final MainAdapterGroup adapter;
        final TextView tvNameGroup;
        private final RelativeLayout expandableLayout;
        private final RecyclerView nestedRecyclerView;
        private final TextView clickExpandableGroup;
        public final ImageView imageViewClickEditNameGroup;
        private final LinearLayout mainParentLinear;
        private final CardView cardView;

        public ContactViewHolder(@NonNull View itemView, MainAdapterGroup adapter) {
            super(itemView);
            this.adapter = adapter;
            tvNameGroup = itemView.findViewById(R.id.main_itemTv);
            expandableLayout = itemView.findViewById(R.id.main_expandable_layout);
            nestedRecyclerView = itemView.findViewById(R.id.main_child_rv);
            clickExpandableGroup = itemView.findViewById(R.id.tv_show_subgroup);
            imageViewClickEditNameGroup = itemView.findViewById(R.id.main_edit_group_imageview);
            mainParentLinear = itemView.findViewById(R.id.main_parent_linear_layout_of_parent_adapter);
            cardView = itemView.findViewById(R.id.mainCardView);


            /**
             * ClickListener получает id группы и возвращает в фрагмент для получения списка
             * контактов данной группы
             */
            tvNameGroup.setOnClickListener(v -> {
                int idGroup = listOfGroup.get(getLayoutPosition()).getId();
                iMainAdapterListener.openContactsForThisGroupOrSubgroup(idGroup, 0);
            });

            imageViewClickEditNameGroup.setOnClickListener(v -> {
                iMainAdapterListener.editNameGroupOrSubgroup(
                        tvNameGroup.getText().toString(),
                        0,
                        listOfGroup.get(getLayoutPosition()).getId());
            });


        }


    }
}
