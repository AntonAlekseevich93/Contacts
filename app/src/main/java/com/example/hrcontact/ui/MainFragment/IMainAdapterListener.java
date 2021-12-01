package com.example.hrcontact.ui.MainFragment;

public interface IMainAdapterListener {
    void openContactsForThisGroupOrSubgroup(int id, int type);
    void editNameGroupOrSubgroup(String name, int type, int id);
}
