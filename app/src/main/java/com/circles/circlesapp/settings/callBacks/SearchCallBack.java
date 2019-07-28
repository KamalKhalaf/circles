package com.circles.circlesapp.settings.callBacks;

/**/

import java.util.ArrayList;

import com.circles.circlesapp.search.SearchFragment;
import com.circles.circlesapp.search.SearchResult;

public interface SearchCallBack extends BaseCallBack{
    void loadData(ArrayList<SearchResult> results);
    SearchFragment getFragment();
}
