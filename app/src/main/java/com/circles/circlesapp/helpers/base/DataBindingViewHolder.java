package com.circles.circlesapp.helpers.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.circles.circlesapp.BR;


/**
 * Created by Mohamed Khaled on Tue, 30/Oct/2018 at 2:06 PM.
 * <p>
 * mohamed.khaled@apptcom.com
 * linkedin.com/in/mohamed5aled
 */
public class DataBindingViewHolder<T,B extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final B b;

    public DataBindingViewHolder(B b) {
        super(b.getRoot());
        this.b = b;
    }

    public void bind(T item) {
        b.setVariable(BR.obj, item);
    }

    public void bind(BaseListAdapter adapter) {
        b.setVariable(BR.adapter, adapter);
        b.executePendingBindings();
    }
}
