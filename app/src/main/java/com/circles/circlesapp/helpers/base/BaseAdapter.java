package com.circles.circlesapp.helpers.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Mohamed Khaled on Tue, 14/Aug/2018 at 2:22 PM.
 * <p>
 * mohamed.khaled@apptcom.com
 * linkedin.com/in/mohamed5aled
 */
public abstract class BaseAdapter
        extends RecyclerView.Adapter<BindingViewHolder> {
    public BindingViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false);
        BindingViewHolder viewHolder = new BindingViewHolder(binding);
        viewHolder.bind(this);
        return viewHolder;
    }

    public void onBindViewHolder(BindingViewHolder holder,
                                 int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);
        Log.i("BaseAdapter", "onBindViewHolder");
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);
}