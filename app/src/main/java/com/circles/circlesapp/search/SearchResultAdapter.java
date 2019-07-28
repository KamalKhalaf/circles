package com.circles.circlesapp.search;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.SearchResultItemViewBinding;
import com.circles.circlesapp.BR;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.Holder> {
    int pos;
    private List<SearchResult> data;

    public interface SearchResultListener {
        void onClick(SearchResult item, int pos);
    }

    private SearchResultListener listener;

    public SearchResultAdapter() {
        data = new ArrayList<>();
    }

    public void setListener(SearchResultListener listener) {
        this.listener = listener;
    }

    public void setData(List<SearchResult> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(SearchResult item) {
        data.add(item);
        // notifyDataSetChanged();
        notifyItemInserted(data.size() - 1);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchResultItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.search_result_item_view, parent, false);
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
        SearchResultItemViewBinding itemViewBinding;

        public Holder(SearchResultItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;

        }

        public void bind(SearchResult product) {
            itemViewBinding.setVariable(BR.searchItem, product);
            itemViewBinding.executePendingBindings();
            itemViewBinding.getRoot().setOnClickListener((v) ->
                    listener.onClick(product, getAdapterPosition())
            );
        }
    }
}