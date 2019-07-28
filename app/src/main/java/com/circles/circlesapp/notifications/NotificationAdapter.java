package com.circles.circlesapp.notifications;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.NotificationItemViewBinding;

import java.util.ArrayList;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {
    int pos;
    private List<NotificationModel> data;

    public interface NotificationListener {
        void onClick(NotificationModel item, int pos);
    }

    private NotificationListener listener;

    public NotificationAdapter() {
        data = new ArrayList<>();
    }

    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    public void setData(List<NotificationModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(NotificationModel item) {
        data.add(item);
        // notifyDataSetChanged();
        notifyItemInserted(data.size() - 1);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_item_view, parent, false);
        return new Holder(binding);
    }

    public int currentPos() {
        return pos;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        pos = position;
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        NotificationItemViewBinding itemViewBinding;

        public Holder(NotificationItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;

        }

        public void bind(NotificationModel product) {
            itemViewBinding.setNotificationModel(product);
            itemViewBinding.getRoot().setOnClickListener((v) ->
                    listener.onClick(product, getAdapterPosition())
            );
        }
    }
}