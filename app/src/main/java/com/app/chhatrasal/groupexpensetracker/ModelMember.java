package com.app.chhatrasal.groupexpensetracker;

import java.util.ArrayList;

/**
 * Created by chhat on 11-05-2017.
 */
public class ModelMember {
    private String memberName;
    private int total;

    public ModelMember() {
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
