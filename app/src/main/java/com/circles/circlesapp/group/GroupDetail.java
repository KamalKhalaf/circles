package com.circles.circlesapp.group;

import java.util.ArrayList;
import java.util.List;

public class GroupDetail {

    public String name;
    private List<String> covers;

    public List<String> getCovers() {
        return (covers == null) ? new ArrayList<>() : covers;
    }
}
