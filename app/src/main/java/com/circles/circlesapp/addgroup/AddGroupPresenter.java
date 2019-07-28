package com.circles.circlesapp.addgroup;

import android.support.annotation.NonNull;

import com.circles.circlesapp.helpers.core.ApiResponseCallBack;

public class AddGroupPresenter implements Contractor.presenter {
    private AddGroupRepo repo;
    private Contractor.view view;

    AddGroupPresenter(Contractor.view view) {
        this.view = view;
        repo = new AddGroupRepo();
    }

    @Override
    public void addNewGroup(AddGroupBody body, GroupTypes type) {
        if (body.getLocation() == null) {
            view.onFailed("your location is not available please try again");
            return;
        }
        switch (type) {
            case PUBLIC_GROUP:
                repo.postNewGroup(body, getResp(false));
                break;
            case PRIVATE_GROUP:
                repo.postNewPrivateGroup(body, getResp(true));
                break;
            case EVENT:
                repo.postNewEvent(body, getResp(true));

        }

    }

    @NonNull
    private ApiResponseCallBack<String> getResp(boolean showWebView) {
        return new ApiResponseCallBack<String>() {
            @Override
            public void success(String s) {
                if (showWebView) {
                    view.showWebView(s);
                }else {
                    view.onGroupAddedSuccessfully();
                }

            }

            @Override
            public void fail(String message) {
                view.onFailed(message);
            }
        };
    }
}
