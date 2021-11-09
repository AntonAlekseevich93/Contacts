package com.example.contacts.screens.selectGroupFragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.entity.GroupContacts;

import java.util.List;
import java.util.function.Function;

public class SelectGroupAdapter extends RecyclerView.Adapter<SelectGroupAdapter.SelectContactViewHolder> {
    private Context context;
    private List<GroupContacts> groupContactsList;
    private Function<String, Void> function;

    public SelectGroupAdapter(Context context, List<GroupContacts> groupContactsList, Function<String, Void> function) {
        this.context = context;
        this.groupContactsList = groupContactsList;
        this.function = function;
    }

    public void setList(List<GroupContacts> groupContactsList) {
        this.groupContactsList = groupContactsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_group_contact_item, parent, false);
        return new SelectContactViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectContactViewHolder holder, int position) {
        if (groupContactsList.get(position).isSelect())
            holder.linearLayoutForSetBackground.setBackgroundColor(context.getColor(R.color.colorApp));
        else holder.linearLayoutForSetBackground.setBackgroundColor(0);

        holder.tvNameGroup.setText(groupContactsList.get(position).getNameGroup());

    }

    @Override
    public int getItemCount() {
        return groupContactsList.size();
    }

    public class SelectContactViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNameGroup;
        final RelativeLayout relativeLayoutClickAtItem;
        final SelectGroupAdapter adapter;
        final LinearLayout linearLayoutForSetBackground;

        public SelectContactViewHolder(@NonNull View itemView, SelectGroupAdapter adapter) {
            super(itemView);
            tvNameGroup = itemView.findViewById(R.id.tvNameGroup);
            linearLayoutForSetBackground = itemView.findViewById(R.id.linearLayoutCreateGroupParent);
            relativeLayoutClickAtItem = itemView.findViewById(R.id.relativeLayoutNameGroup);
            this.adapter = adapter;
            relativeLayoutClickAtItem.setOnClickListener(v -> {
//                function.apply(tvNameGroup.getText().toString());

                if (!groupContactsList.get(getLayoutPosition()).isSelect()) {
                    groupContactsList.get(getLayoutPosition()).setSelect(true);
                } else {
                    groupContactsList.get(getLayoutPosition()).setSelect(false);
                }
                notifyItemChanged(getLayoutPosition());
            });

        }
    }
}
