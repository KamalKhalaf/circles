package com.circles.circlesapp.home;



import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.BottomNavigationBarLayoutBinding;

public class NavigationBottomActivity extends AppCompatActivity{
     private BottomNavigationBarLayoutBinding layoutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding= DataBindingUtil.setContentView(this, R.layout.bottom_navigation_bar_layout);
        setContentView(layoutBinding.getRoot());

    }
}
