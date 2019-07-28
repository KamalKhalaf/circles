package com.circles.circlesapp.profile.model;
/**/

import java.util.ArrayList;
import java.util.List;

public class FollowerList {


    /**
     * status : Success.
     * data : [{"id":5,"first_name":"John","last_name":"Watson","username":"2g53MI","profile_image":null},{"id":3,"first_name":"Hisham","last_name":"Magdy","username":"BnMagdy","profile_image":null},{"id":24,"first_name":"kamal","last_name":"khalaf","username":"kamal1111","profile_image":"http://ec2-18-216-242-74.us-east-2.compute.amazonaws.com/circles/public/storage/media/SUPpxutaTlRcbbg05GGLgAl31n7ee7coa9u8xKv7.jpeg"},{"id":25,"first_name":"abdulrahman","last_name":"alzahrani","username":"pqw4","profile_image":"http://ec2-18-216-242-74.us-east-2.compute.amazonaws.com/circles/public/storage/media/TbJKhdQcZxJ1haLI5OONRWN6xY1V9DJBznmbhmVs.jpeg"},{"id":29,"first_name":"kkkk","last_name":"llllll","username":"kemo","profile_image":"http://ec2-18-216-242-74.us-east-2.compute.amazonaws.com/circles/public/storage/media/glmKNlkMvfgpRePelw6V4uNXw0n3GcJSCM75zIUz.jpeg"}]
     */

    private String status;
    private ArrayList<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5
         * first_name : John
         * last_name : Watson
         * username : 2g53MI
         * profile_image : null
         */

        private int id;
        private String first_name;
        private String last_name;
        private String username;
        private Object profile_image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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

        public Object getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(Object profile_image) {
            this.profile_image = profile_image;
        }
    }
}
