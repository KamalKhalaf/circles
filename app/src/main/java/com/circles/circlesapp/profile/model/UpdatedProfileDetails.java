package com.circles.circlesapp.profile.model;
/**/

public class UpdatedProfileDetails {


    /**
     * message : Success
     * data : {"first_name":"Khalid","last_name":"Ghanim","username":"khalid.ghanim@hotmail.com","profile_image":null,"age":27,"following":5,"followers":4,"description":null,"city":"Giza","country":"Egypt","social_accounts":{"facebook":"kamal.khalaf56@gmail.com","twitter":"q_masoudd","whatsapp":null,"linkedin":null,"youtube":null,"instagram":"paeek@\u20ac#&&"}}
     */

    private String message;
    private DataBean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * first_name : Khalid
         * last_name : Ghanim
         * username : khalid.ghanim@hotmail.com
         * profile_image : null
         * age : 27
         * following : 5
         * followers : 4
         * description : null
         * city : Giza
         * country : Egypt
         * social_accounts : {"facebook":"kamal.khalaf56@gmail.com","twitter":"q_masoudd","whatsapp":null,"linkedin":null,"youtube":null,"instagram":"paeek@\u20ac#&&"}
         */

        private String first_name;
        private String last_name;
        private String username;
        private String profile_image;
        private int age;
        private int following;
        private int followers;
        private String description;
        private String city;
        private String country;
        private SocialAccountsBean social_accounts;

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getFollowing() {
            return following;
        }

        public void setFollowing(int following) {
            this.following = following;
        }

        public int getFollowers() {
            return followers;
        }

        public void setFollowers(int followers) {
            this.followers = followers;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
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
            private Object whatsapp;
            private Object linkedin;
            private Object youtube;
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

            public Object getWhatsapp() {
                return whatsapp;
            }

            public void setWhatsapp(Object whatsapp) {
                this.whatsapp = whatsapp;
            }

            public Object getLinkedin() {
                return linkedin;
            }

            public void setLinkedin(Object linkedin) {
                this.linkedin = linkedin;
            }

            public Object getYoutube() {
                return youtube;
            }

            public void setYoutube(Object youtube) {
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
}
