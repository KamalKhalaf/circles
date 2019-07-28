package com.circles.circlesapp.settings;

/**/

import com.google.gson.annotations.SerializedName;

public  class SocialMedia {
    @SerializedName("instagram")
    private String instagram;
    @SerializedName("youtube")
    private String youtube;
    @SerializedName("linkedin")
    private String linkedin;
    @SerializedName("whatsapp")
    private String whatsapp;
    @SerializedName("twitter")
    private String twitter;
    @SerializedName("facebook")
    private String facebook;

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @Override
    public String toString() {
        return "SocialMedia{" +
                "instagram='" + instagram + '\'' +
                ", youtube='" + youtube + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", twitter='" + twitter + '\'' +
                ", facebook='" + facebook + '\'' +
                '}';
    }
}
