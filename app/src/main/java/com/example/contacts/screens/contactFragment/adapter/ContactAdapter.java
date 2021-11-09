package com.example.contacts.screens.contactFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.entity.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context context;
    private List<Contact> listOfContacts;

    public ContactAdapter(Context context, List<Contact> listOfContacts) {
        this.context = context;
        this.listOfContacts = listOfContacts;
    }

    public void setList(List<Contact> listOfContacts) {
        this.listOfContacts = listOfContacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_contact_item, parent, false);
        return new ContactViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        if (listOfContacts != null) {
            holder.tvNameContact.setText(listOfContacts.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        if (listOfContacts != null) {
            return listOfContacts.size();
        } else return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvNameContact;
        final ContactAdapter adapter;

        public ContactViewHolder(@NonNull View itemView, ContactAdapter adapter) {
            super(itemView);
            this.tvNameContact = itemView.findViewById(R.id.tvNameContact);
            this.adapter = adapter;
        }
    }
}
