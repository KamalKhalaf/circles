
package com.circles.circlesapp.profile;


import android.databinding.BaseObservable;

import org.jetbrains.annotations.NotNull;

public class ProfileData extends BaseObservable {
    public String first_name;
    public String last_name;
    public String username;
    public String age;
    public String following;
    public String followers;
    public String description;
    public String country = "";
    public String city = "";
    public String profile_image;
    /**
     * social_accounts : {"facebook":"kamal.khalaf56@gmail.com","twitter":"q_masoudd","whatsapp":null,"linkedin":null,"youtube":null,"instagram":"paeek@\u20ac#&&"}
     */

    private SocialAccountsBean social_accounts;


    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getAge() {
        return "age:" + age;
    }

    public String getLoc() {
        return "Location : " + getCountry() + " , " + getCity();
    }

    public String getCountry() {
        return (country == null) ? "" : country;
    }

    public String getCity() {
        return (city == null) ? "" : city;
    }

    @NotNull
    public String getName() {
        return first_name + " " + last_name;
    }

    public String getUsername() {
        return "@" + username;
    }

    public SocialAccountsBean getSocial_accounts() {
        return social_accounts;
    }

    public void setSocial_accounts(SocialAccountsBean social_accounts) {
        this.social_accounts = social_accounts;
    }

    public static class SocialAccountsBean {
        /**
         * facebook : kamal.khalaf56@gmail.com
         * twitter : q_masoudd
         * whatsapp : null
         * linkedin : null
         * youtube : null
         * instagram : paeek@€#&&
         */

        private String facebook;
        private String twitter;
        private String whatsapp;
        private String linkedin;
        private String youtube;
        private String instagram;

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public void setWhatsapp(String whatsapp) {
            this.whatsapp = whatsapp;
        }

        public String getLinkedin() {
            return linkedin;
        }

        public void setLinkedin(String linkedin) {
            this.linkedin = linkedin;
        }

        public String getYoutube() {
            return youtube;
        }

        public void setYoutube(String youtube) {
            this.youtube = youtube;
        }

        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }
    }
}
