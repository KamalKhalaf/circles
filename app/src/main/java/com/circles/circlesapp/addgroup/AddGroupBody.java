package com.circles.circlesapp.addgroup;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddGroupBody {
    private List<File> fileList;
    private String name;
    private String password;
    private String descriptiom;
    private LatLng location;

    public List<File> getFileList() {
        if (fileList == null) {
            return new ArrayList<>();
        }
        return fileList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptiom() {
        return descriptiom;
    }

    public void setDescriptiom(String descriptiom) {
        this.descriptiom = descriptiom;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setFileListFronUri(List<String> paths) {
        if (paths == null) return;
        List<File> files = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            File f = new File(paths.get(i));
            files.add(f);
        }
        setFileList(files);
    }
}
