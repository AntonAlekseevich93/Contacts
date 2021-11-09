package com.example.contacts.screens.groupFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.entity.GroupContacts;
import com.example.contacts.screens.dialogFragmentContact.DialogFragmentContacts;

import java.util.List;
import java.util.function.Function;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ContactViewHolder> {
    private List<GroupContacts> listOfGroupContact;
    private Context context;
    private LayoutInflater inflater;
    private Function<String, Void> clickListener;
    private Function<String, Void> clickListenerEditGroup;
    private ImageView imageViewEditGroupNameButton;


    public GroupAdapter(Context context, List<GroupContacts> listOfGroupContact, Function<String, Void> clickListener, Function<String, Void> clickListenerEditGroup) {
        this.listOfGroupContact = listOfGroupContact;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.clickListenerEditGroup = clickListenerEditGroup;
    }

    public void setListOfContact(List<GroupContacts> listOfGroupContact) {
        this.listOfGroupContact = listOfGroupContact;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_group_contact_item, parent, false);
        return new ContactViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (listOfGroupContact != null) {
            holder.tvName.setText(listOfGroupContact.get(position).getNameGroup());

        }
    }


    @Override
    public int getItemCount() {
        if (listOfGroupContact != null) {
            return listOfGroupContact.size();
        } else return 0;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvName;
        final GroupAdapter adapter;
        public final RelativeLayout viewClickGroup;
        public final ImageView imageViewClickEditNameGroup;

        public ContactViewHolder(@NonNull View itemView, GroupAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            viewClickGroup = itemView.findViewById(R.id.relativeLayoutNameGroup);
            imageViewClickEditNameGroup = itemView.findViewById(R.id.imageViewEditButton);
            tvName = itemView.findViewById(R.id.tvNameGroup);
            viewClickGroup.setOnClickListener(v->{
                clickListener.apply(tvName.getText().toString());
            });
            imageViewClickEditNameGroup.setOnClickListener(v->{
                clickListenerEditGroup.apply(tvName.getText().toString());
            });


        }


    }
}
