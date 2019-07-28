package com.circles.circlesapp.group.groupMember;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import com.circles.circlesapp.BR;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.MemberItemViewBinding;
import com.circles.circlesapp.helpers.SharedPrefHelper;



public class GroupMemeberAdapter extends RecyclerView.Adapter<GroupMemeberAdapter.Holder> {
    private int pos;
    private List<MemberItem> data;
    private Context context;
    private SharedPrefHelper prefHelper;
    public interface GroupMemeberListener {
        void onClick(MemberItem item, int pos);
    }

    private GroupMemeberListener listener;

    public GroupMemeberAdapter() {
        data = new ArrayList<>();

    }

    public void setListener(GroupMemeberListener listener) {
        this.listener = listener;
    }

    public void setData(List<MemberItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(MemberItem item) {
        data.add(item);
        // notifyDataSetChanged();
        notifyItemInserted(data.size() - 1);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        prefHelper = new SharedPrefHelper(context);

        MemberItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.member_item_view, parent, false);
        return new Holder(binding);
    }

    public int currentPos() {
        return pos;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        pos = position;
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.mute_backg));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));

        }
        MemberItem memberItem = data.get(position);
        if (prefHelper.getMutedSet().contains(memberItem.getMemberId() + "")) {
            holder.itemViewBinding.muteUnmuteImg.setImageDrawable(context.getResources().getDrawable(R.drawable.un_mute));
        } else {
            holder.itemViewBinding.muteUnmuteImg.setImageDrawable(context.getResources().getDrawable(R.drawable.mute));
        }
        holder.bind(memberItem);

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        MemberItemViewBinding itemViewBinding;

        public Holder(MemberItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;

        }

        public void bind(MemberItem product) {
            itemViewBinding.setVariable(BR.memberItem, product);
            itemViewBinding.executePendingBindings();

            itemViewBinding.getRoot().setOnClickListener((v) -> {
                EventBus.getDefault().postSticky(new MemberItem());
                MemberItem memberItem = data.get(getAdapterPosition());
                if (prefHelper.getMutedSet().contains(memberItem.getMemberId() + "")) {
                    prefHelper.removeMutedId(memberItem.getMemberId());
                    itemViewBinding.muteUnmuteImg.setImageDrawable(context.getResources().getDrawable(R.drawable.mute));
                } else {
                    prefHelper.addMutedId(memberItem.getMemberId());
                    itemViewBinding.muteUnmuteImg.setImageDrawable(context.getResources().getDrawable(R.drawable.un_mute));
                }
            });
        }
    }
}