package com.example.contacts.screens.selectSubGroupFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.entity.SubGroupContact;
import com.example.contacts.screens.selectGroupFragment.adapter.SelectGroupAdapter;

import java.util.List;
import java.util.function.Function;

public class SelectedSubGroupAdapter extends RecyclerView.Adapter<SelectedSubGroupAdapter.SelectSubContactViewHolder> {
    private Context context;
    private List<SubGroupContact> subGroupContacts;
    private Function<String, Void> function;

    public SelectedSubGroupAdapter(Context context, List<SubGroupContact> subGroupContacts, Function<String, Void> function) {
        this.context = context;
        this.subGroupContacts = subGroupContacts;
        this.function = function;
    }

    public void setList(List<SubGroupContact> subGroupContacts) {
        this.subGroupContacts = subGroupContacts;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SelectSubContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_group_contact_item, parent, false);
        return new SelectSubContactViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectSubContactViewHolder holder, int position) {
        holder.tvNameSubGroup.setText(subGroupContacts.get(position).getNameSubGroup());
    }

    @Override
    public int getItemCount() {
        return subGroupContacts.size();
    }

    public class SelectSubContactViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNameSubGroup;
        final SelectedSubGroupAdapter adapter;

        public SelectSubContactViewHolder(@NonNull View itemView, SelectedSubGroupAdapter adapter) {
            super(itemView);
            tvNameSubGroup = itemView.findViewById(R.id.tvNameGroup);
            tvNameSubGroup.setOnClickListener(v -> {
                function.apply(tvNameSubGroup.getText().toString());
            });
            this.adapter = adapter;
        }
    }
}

