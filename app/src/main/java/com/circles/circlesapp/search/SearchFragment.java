package com.circles.circlesapp.search;



import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.SearchResultLayoutBinding;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.profile.UserProfileActivity;
import com.circles.circlesapp.settings.callBacks.SearchCallBack;
import com.circles.circlesapp.settings.viewModels.search.SearchViewModel;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class SearchFragment extends Fragment implements SearchResultAdapter.SearchResultListener, SearchCallBack {
    public static final String SEARCHQUERY = "SearchQuery";
    private SearchResultLayoutBinding layoutBinding;
    private String searchquery;
    private Disposable disposable;
    private SearchResultAdapter adapter;
    private SearchViewModel viewModel;


    public static SearchFragment getInstance(String query) {
        Bundle bundle = new Bundle();
        bundle.putString(SEARCHQUERY, query);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = DataBindingUtil.inflate(inflater, R.layout.search_result_layout, container, false);
        searchquery = getArguments().getString(SEARCHQUERY);
//        Toast.makeText(getContext(), searchquery, Toast.LENGTH_SHORT).show();
        setUpAdapter();
        return layoutBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.attachView(this);
        viewModel.reqSearch(searchquery);
        layoutBinding.setSearch(viewModel);
    }

    private void setUpAdapter() {
        adapter = new SearchResultAdapter();
        adapter.setListener(this);
        layoutBinding.searchRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        disposable = RxBus.get().subscribeSearchQuery(this::setSearchquery);
    }

    public void setSearchquery(String searchquery) {
        viewModel.reqSearch(searchquery);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxBus.get().unSubscribe(disposable);
    }

    @Override
    public void onClick(SearchResult item, int pos) {
        Log.d("", "onClick: " + item);
        switch (item.getType()) {

            case "user":
                if (MyServiceInterceptor.userId == item.getId()) {
                    return;
                } else {
                    UserProfileActivity.start(getContext(), item.getId());
                }
                break;
            case "private":
                SearchGroupMapActivity.start(getActivity(), item);
                break;
            case "public":
                SearchGroupMapActivity.start(getActivity(), item);
                break;
            case "event":
                SearchGroupMapActivity.start(getActivity(), item);
                break;
        }
    }

    @Override
    public void loadData(ArrayList<SearchResult> results) {
        adapter.setData(results);
    }

    @Override
    public SearchFragment getFragment() {
        return this;
    }
}
