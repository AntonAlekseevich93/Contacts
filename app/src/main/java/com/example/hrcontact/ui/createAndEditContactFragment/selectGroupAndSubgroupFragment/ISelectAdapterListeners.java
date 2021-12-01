package com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment;

public interface ISelectAdapterListeners {
    void setCountSelectedGroup(Integer idGroup, Boolean action);
    void setIdAndNameFromSelectedGroupOrSubgroup(Integer id, String name, Integer type);
    void createNewSubgroup(Integer idGroup);
}
