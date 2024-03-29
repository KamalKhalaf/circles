package com.circles.circlesapp.helpers.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.circles.circlesapp.BR;

/**
 * Created by Mohamed Khaled on Tue, 14/Aug/2018 at 2:26 PM.
 * <p>
 * mohamed.khaled@apptcom.com
 * linkedin.com/in/mohamed5aled
 */
public class BindingViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding binding;

    public BindingViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.obj, obj);
    }

    public void bind(BaseAdapter adapter) {
        binding.setVariable(BR.adapter, adapter);
        binding.executePendingBindings();
    }

}