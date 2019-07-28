package com.circles.circlesapp.addgroup;

public interface Contractor {
    interface presenter{
         void addNewGroup(AddGroupBody body, GroupTypes type);
    }
    interface view{
        void onGroupAddedSuccessfully();
        void onFailed(String message);
        void showWebView(String url);
    }
}
