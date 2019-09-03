package com.circles.circlesapp.phase2.views.adapter;
/*
 *
 * Created by Kamal Khalaf on 8/27/2019.
 * Contact : kamal.khalaf56@gmail.com
 *
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ItemSocialMediaBinding;
import com.circles.circlesapp.phase2.views.ui.profile.SocialMediaModel;

import java.util.List;

public class ListOfSocialMediasAdapter extends RecyclerView.Adapter<ListOfSocialMediasAdapter.ViewHolder> {
    private List<SocialMediaModel> items;


    public ListOfSocialMediasAdapter(List<SocialMediaModel> models) {
        this.items = models;
    }

    ///////////////to identify the layout context like  activity oncreate or fragment
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemSocialMediaBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_social_media, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      holder.binding.setModel(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemSocialMediaBinding binding;

        ViewHolder(ItemSocialMediaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
