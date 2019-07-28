package com.circles.circlesapp.profile;
/**/

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.FollowItemBinding;
import com.circles.circlesapp.profile.model.FollowerList;

import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    ArrayList<FollowerList.DataBean> dataBeans;
    Context context;
    private boolean isTrue;

    public FollowAdapter(Context context, ArrayList<FollowerList.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        this.context = context;
        isTrue = false;
    }

    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        FollowItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.follow_item, parent, false);
        return new FollowAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (dataBeans.get(position).getFirst_name() != null && dataBeans.get(position).getLast_name() != null)
            holder.binding.tvName.setText(dataBeans.get(position).getFirst_name() + " " + dataBeans.get(position).getLast_name());
        holder.binding.tvUserName.setText("@" + dataBeans.get(position).getUsername());


        holder.binding.executePendingBindings();

        isTrue = false;

        if (dataBeans.get(position).getProfile_image() != null) {

            Glide.with(context).load(dataBeans.get(position).getProfile_image()).listener(new RequestListener<Drawable>() {

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.binding.progressBarload.setVisibility(View.GONE);
                    isTrue = true;
                    return false;

                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                    holder.binding.progressBarload.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.binding.ivUserImage);


        } else {
            Glide.with(context).load(R.drawable.no_image).into(holder.binding.ivUserImage);
        }

        if (isTrue) {
            holder.binding.ivUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image));

        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataBeans.get(position).getId() != 0) {
                    UserProfileActivity.start(context, dataBeans.get(position).getId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataBeans == null ? 0 : dataBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final FollowItemBinding binding;

        public ViewHolder(FollowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

