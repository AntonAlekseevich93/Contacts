package com.example.hrcontact.ui.createAndEditContactFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCreateContactFragment extends RecyclerView
        .Adapter<AdapterCreateContactFragment.CreateHolder> {

    private final Context context;
    private List<String> valuesList = new ArrayList<>();

    public AdapterCreateContactFragment(Context context) {
        this.context = context;
    }

    public void setNameSelectedGroupOrSubgroup(List<String> list) {
        valuesList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CreateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_selected_group_show_tags_for_create_fragment, parent, false);
        return new CreateHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateHolder holder, int position) {
        String symbol_tags = context.getResources().getString(R.string.tags_symbol);
        String tag = symbol_tags + valuesList.get(position);
        holder.textView.setText(tag);
    }


    @Override
    public int getItemCount() {
        return valuesList.size();
    }


    public class CreateHolder extends RecyclerView.ViewHolder {
        final AdapterCreateContactFragment adapter;
        final TextView textView;

        public CreateHolder(@NonNull View itemView, AdapterCreateContactFragment adapter) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvTagForSelectGroup);
            this.adapter = adapter;
        }
    }
}
