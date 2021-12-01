package com.example.hrcontact.support;

public interface IClickListenerAdapterContact {
    void update();
    void openEditContact(int idContact);
    void openInfoContact(String number ,String nameContact, String descriptionContact);
    void deleteContact(int idContact);
    void shareContact(String number, String nameContact, String description);
}
