package com.circles.circlesapp.helpers.ui;

public interface RecyclerCallback<VM, T> {

    void bindData(VM binding, T model, int position);

    void onItemClicked(VM binding, T model, int position);

}
