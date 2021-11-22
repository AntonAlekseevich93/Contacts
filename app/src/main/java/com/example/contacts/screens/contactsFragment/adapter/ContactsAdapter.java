package com.example.contacts.screens.contactsFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.entity.Contact;
import com.example.contacts.entity.ContactWithGroups;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private Context context;
    private List<ContactWithGroups> listOfContacts;

    public ContactsAdapter(Context context, List<ContactWithGroups> listOfContacts) {
        this.context = context;
        this.listOfContacts = listOfContacts;
    }

    public void setList(List<ContactWithGroups> listOfContacts) {
        this.listOfContacts = listOfContacts;
        notifyDataSetChanged();
    }

    public void clearList() {
        listOfContacts.clear();
    }


    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_contact_item, parent, false);
        return new ContactsViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        if (listOfContacts != null) {
            holder.tvNameContact.setText(listOfContacts.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return listOfContacts.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNameContact;
        final ContactsAdapter adapter;

        public ContactsViewHolder(@NonNull View itemView, ContactsAdapter adapter) {
            super(itemView);
            this.tvNameContact = itemView.findViewById(R.id.tvNameContact);
            this.adapter = adapter;
        }
    }
}
