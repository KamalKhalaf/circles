package com.circles.circlesapp.settings.viewModels.base;



import com.circles.circlesapp.settings.callBacks.BaseCallBack;

public interface BaseVmodel<v extends BaseCallBack> {
    void attachView(v view);
}
