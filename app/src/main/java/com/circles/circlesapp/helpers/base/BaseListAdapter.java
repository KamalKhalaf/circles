package com.circles.circlesapp.helpers.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Mohamed Khaled on Tue, 30/Oct/2018 at 12:54 PM.
 * <p>
 * mohamed.khaled@apptcom.com
 * linkedin.com/in/mohamed5aled
 */
public abstract class BaseListAdapter<T,B extends ViewDataBinding> extends ListAdapter<T, DataBindingViewHolder<T,B>> {
    private Context context;
    public BaseListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public DataBindingViewHolder<T,B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        B binding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false);
        return new DataBindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingViewHolder<T,B> holder, int position) {
        holder.bind(getItem(position));
        holder.bind(this);
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected T getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public abstract int getLayoutIdForPosition(int position);
}
