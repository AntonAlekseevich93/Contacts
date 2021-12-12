package com.example.hrcontact.ui.contactsFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.hrcontact.db.entity.ContactWithGroups;
import com.example.hrcontact.support.IClickListenerAdapterContact;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private final Context context;
    private List<ContactWithGroups> listOfContacts;
    private final IClickListenerAdapterContact iClickListenerAdapterContact;

    public ContactsAdapter(Context context, List<ContactWithGroups> listOfContacts,
                           IClickListenerAdapterContact iClickListenerAdapterContact) {
        this.context = context;
        this.listOfContacts = listOfContacts;
        this.iClickListenerAdapterContact = iClickListenerAdapterContact;
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
        private final ContactsAdapter adapter;

        public ContactsViewHolder(@NonNull View itemView, ContactsAdapter adapter) {
            super(itemView);
            this.tvNameContact = itemView.findViewById(R.id.tvNameContact);
            this.adapter = adapter;
            ImageView imageViewEditContact = itemView.findViewById(R.id.edit_contact_imageview);
            ImageView imageViewInfoContact = itemView.findViewById(R.id.imageViewInfoAboutContact);
            ImageView imageViewDeleteContact = itemView.findViewById(R.id.imageViewDeleteContact);
            ImageView imageViewShareContact = itemView.findViewById(R.id.shareContactImageview);

            imageViewEditContact.setOnClickListener(view -> {
                iClickListenerAdapterContact.openEditContact(listOfContacts.get(getLayoutPosition())
                        .getIdContacts());
            });

            imageViewInfoContact.setOnClickListener(view -> {
                int position = getLayoutPosition();
                iClickListenerAdapterContact.openInfoContact(
                        listOfContacts.get(position).getNumber(),
                        listOfContacts.get(position).getName(),
                        listOfContacts.get(position).getDescription());
            });

            imageViewDeleteContact.setOnClickListener(view -> {
                iClickListenerAdapterContact.deleteContact(listOfContacts.get(getLayoutPosition())
                        .getIdContacts());
            });

            imageViewShareContact.setOnClickListener(view -> {
                int position = getLayoutPosition();
                iClickListenerAdapterContact.shareContact(
                        listOfContacts.get(position).getNumber(),
                        listOfContacts.get(position).getName(),
                        listOfContacts.get(position).getDescription()
                );
            });
        }
    }
}
