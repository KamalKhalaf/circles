package com.circles.circlesapp.settings.socialMedia;


import com.circles.circlesapp.settings.callBacks.SocialMediaCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseVmodel;


public interface SocialMediaVmodel<v extends SocialMediaCallBack> extends BaseVmodel<v> {
    void editFaceBook();
    void editInsta();
    void editTwitter();
    void editSnap();
    void editLinked();
    void editYouTube();
    void deleteFaceBook();
    void deleteInsta();
    void deleteTwitter();
    void deleteSnap();
    void deleteLinked();
    void deleteYouTube();
    void connectFaceBook();
    void connectInsta();
    void connectTwitter();
    void connectSnap();
    void connectLinked();
    void connectYouTube();
    void back();
    void confirmChange();
    void reqGetSocialMedia();
    void reqUpdateSocial();
}
