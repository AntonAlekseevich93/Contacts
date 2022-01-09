package com.example.hrcontact.ui.createAndEditContactFragment.selectGroupAndSubgroupFragment;

public interface IListenerNestedSelectAdapter {
    void setIdAndNameFromSelectedGroup(int idSubGroup, String name, int idGroup, int positionGroup);
    void setNumberOfSelectedSubGroup(int position, boolean increaseOrDecrease);
}
