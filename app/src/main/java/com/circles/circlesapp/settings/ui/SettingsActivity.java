package com.circles.circlesapp.settings.ui;

/*
 * Created By mabrouk on 28/01/19
 * com.circles
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.fragment.NavHostFragment;
import com.circles.circlesapp.R;

public class SettingsActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void openUrl(Context context ,String s) {
        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
        openURL.setData(Uri.parse(s));
        context.startActivity(openURL);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.settings_nav_graph);
        List<Fragment> fragments = new ArrayList<>();
        if (navHostFragment != null) {
            fragments = navHostFragment.getChildFragmentManager().getFragments();
        }
        Log.d("", "onActivityResult: "+fragments);
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof EditProfileFragment) {
                EditProfileFragment f = (EditProfileFragment) fragments.get(i);
                f.onActivityResult(requestCode, resultCode, data);
                return;
            }

        }
        // super.onActivityResult(requestCode, resultCode, data);
    }
}
