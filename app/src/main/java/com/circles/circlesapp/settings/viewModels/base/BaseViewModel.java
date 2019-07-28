package com.circles.circlesapp.settings.viewModels.base;



import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.circles.circlesapp.settings.callBacks.BaseCallBack;

public class BaseViewModel<v extends BaseCallBack> extends AndroidViewModel implements BaseVmodel<v>{
    private v view;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void attachView(v view) {
      this.view=view;
    }

    public v getView() {
        return this.view;
    }
}
