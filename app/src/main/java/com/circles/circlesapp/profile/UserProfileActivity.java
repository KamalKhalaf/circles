package com.circles.circlesapp.profile;

/**/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.UserProfileContainerBinding;
import com.circles.circlesapp.search.RxBus;
import com.circles.circlesapp.search.SearchFragment;

public class UserProfileActivity extends AppCompatActivity {
    private static final String USERID = "userId";
    private UserProfileContainerBinding layoutBinding;
    private NavController mNavController;


    public static void start(Context context,int userId){
        Intent intent=new Intent(context,UserProfileActivity.class);
        intent.putExtra(USERID,userId);
        context.startActivity(intent);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding= DataBindingUtil.setContentView(this, R.layout.user_profile_container);
        setContentView(layoutBinding.getRoot());
        SearchView.SearchAutoComplete autoComplete= layoutBinding.searchView.findViewById(R.id.search_src_text);
        autoComplete.setTextColor(R.color.black);
        autoComplete.setHintTextColor(R.color.black);
        autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
        mNavController= Navigation.findNavController(this,R.id.search_nav_host);
        layoutBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mNavController.getCurrentDestination().getId()==R.id.searchFragment){
                    RxBus.get().sendSearchQuery(query);
                }else {
                    Bundle bundle=new Bundle();
                    bundle.putString(SearchFragment.SEARCHQUERY,query);
                    mNavController.navigate(R.id.action_userProfileFragment_to_searchFragment,bundle);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()&&mNavController.getCurrentDestination().getId()==R.id.searchFragment){
                    mNavController.popBackStack();
                }
                return false;
            }
        });

        layoutBinding.back.setOnClickListener(e ->finish());
    }
}
