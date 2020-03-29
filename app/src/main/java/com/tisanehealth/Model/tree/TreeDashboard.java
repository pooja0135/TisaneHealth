
package com.tisanehealth.Model.tree;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TreeDashboard {

    @SerializedName("LInactiveMember")
    @Expose
    private String lInactiveMember;
    @SerializedName("LeftMember")
    @Expose
    private String leftMember;
    @SerializedName("Lpv")
    @Expose
    private String lpv;
    @SerializedName("RInactiveMember")
    @Expose
    private String rInactiveMember;
    @SerializedName("RightMember")
    @Expose
    private String rightMember;
    @SerializedName("Rpv")
    @Expose
    private String rpv;
    @SerializedName("TeamA")
    @Expose
    private String teamA;
    @SerializedName("TeamB")
    @Expose
    private String teamB;

    public String getLInactiveMember() {
        return lInactiveMember;
    }

    public void setLInactiveMember(String lInactiveMember) {
        this.lInactiveMember = lInactiveMember;
    }

    public String getLeftMember() {
        return leftMember;
    }

    public void setLeftMember(String leftMember) {
        this.leftMember = leftMember;
    }

    public String getLpv() {
        return lpv;
    }

    public void setLpv(String lpv) {
        this.lpv = lpv;
    }

    public String getRInactiveMember() {
        return rInactiveMember;
    }

    public void setRInactiveMember(String rInactiveMember) {
        this.rInactiveMember = rInactiveMember;
    }

    public String getRightMember() {
        return rightMember;
    }

    public void setRightMember(String rightMember) {
        this.rightMember = rightMember;
    }

    public String getRpv() {
        return rpv;
    }

    public void setRpv(String rpv) {
        this.rpv = rpv;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

}
