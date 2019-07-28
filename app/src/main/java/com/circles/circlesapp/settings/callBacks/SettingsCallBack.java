package com.circles.circlesapp.settings.callBacks;



import androidx.navigation.NavController;
import com.circles.circlesapp.settings.ui.SettingsFragment;

public interface SettingsCallBack extends BaseCallBack {
  NavController getNavController();
  SettingsFragment getFragment();
}
