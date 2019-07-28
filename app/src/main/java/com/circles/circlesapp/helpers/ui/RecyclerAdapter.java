package com.circles.circlesapp.helpers.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public  class RecyclerAdapter<T, VM extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerAdapter<T,VM>.RecyclerViewHolder> {
    private final Context context;
    private List<T> items;
    private int layoutId;
    private RecyclerCallback<VM, T> bindingInterface;

    public RecyclerAdapter(Context context, List<T> items, int layoutId, RecyclerCallback<VM, T> bindingInterface) {
        this.items = items;
        this.context = context;
        this.layoutId = layoutId;
        this.bindingInterface = bindingInterface;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        VM binding;

        RecyclerViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.executePendingBindings();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bindingInterface.onItemClicked(binding,items.get(getAdapterPosition()),getAdapterPosition());
                }
            });

        }

        void bindData(T model) {
            bindingInterface.bindData(binding, model,getAdapterPosition());
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        T item = items.get(position);
        holder.bindData(item);
    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }
}
