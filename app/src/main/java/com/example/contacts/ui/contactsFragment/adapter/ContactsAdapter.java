package com.example.contacts.ui.contactsFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.db.entity.ContactWithGroups;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private Context context;
    private List<ContactWithGroups> listOfContacts;
    private Function<Integer, Void> funClickListenerContact;
    private BiFunction<String, String, Void> funListenerInfoContact;
    private Function<Integer, Void> funListenerDelete;

    public ContactsAdapter(Context context, List<ContactWithGroups> listOfContacts,
                           Function<Integer, Void> funClickListenerContact,
                           BiFunction<String, String, Void> funListenerInfoContact,
                           Function<Integer, Void> funListenerDelete) {
        this.context = context;
        this.listOfContacts = listOfContacts;
        this.funClickListenerContact = funClickListenerContact;
        this.funListenerInfoContact = funListenerInfoContact;
        this.funListenerDelete = funListenerDelete;
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
        private final ImageView imageViewEditContact;
        private final ImageView imageViewInfoContact;
        private final ImageView imageViewDeleteContact;
        final ContactsAdapter adapter;

        public ContactsViewHolder(@NonNull View itemView, ContactsAdapter adapter) {
            super(itemView);
            this.tvNameContact = itemView.findViewById(R.id.tvNameContact);
            imageViewEditContact = itemView.findViewById(R.id.edit_contact_imageview);
            imageViewInfoContact = itemView.findViewById(R.id.imageViewInfoAboutContact);
            imageViewDeleteContact = itemView.findViewById(R.id.imageViewDeleteContact);
            this.adapter = adapter;

            imageViewEditContact.setOnClickListener(view -> {
                funClickListenerContact.apply(listOfContacts.get(getLayoutPosition()).getIdContacts());
            });

            imageViewInfoContact.setOnClickListener(view -> {
                funListenerInfoContact.apply(listOfContacts.get(getLayoutPosition()).getName(),
                        listOfContacts.get(getLayoutPosition()).getDescription());
            });

            imageViewDeleteContact.setOnClickListener(view -> {
                funListenerDelete.apply(listOfContacts.get(getLayoutPosition()).getIdContacts());
            });
        }
    }
}
