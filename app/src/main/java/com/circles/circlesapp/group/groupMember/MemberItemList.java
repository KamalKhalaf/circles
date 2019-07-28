package com.circles.circlesapp.group.groupMember;

import java.util.ArrayList;
import java.util.List;

public class MemberItemList {
    private List<MemberItem> memberItemList;
    private static MemberItemList inst;

    public static MemberItemList getInst() {
        if (inst == null) {
            return inst = new MemberItemList();
        }
        return inst;
    }

   public void addToList(MemberItem m) {
        if (memberItemList != null) {
            memberItemList.add(m);
        } else {
            memberItemList = new ArrayList<>();
            memberItemList.add(m);
        }
    }

  public   void clearList() {
        if (memberItemList != null) memberItemList.clear();
    }

    public List<MemberItem> getList() {
        return (memberItemList==null)?new ArrayList<>():memberItemList;
    }
}
