package com.AFiMOBILE.afslmobileapplication;

import android.app.Application;

public class GlobleClassDetails extends Application {
    private String  Userid;
    private String  OfficerName;
    private String  LoginBranch;
    private String  LoginDate;
    private String  PHP_Path;

    public String getPHP_Path() {
        return PHP_Path;
    }

    public void setPHP_Path(String Inp_PHP_Path) {
        PHP_Path = Inp_PHP_Path;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getOfficerName() {
        return OfficerName;
    }

    public void setOfficerName(String officerName) {
        OfficerName = officerName;
    }

    public String getLoginBranch() {
        return LoginBranch;
    }

    public void setLoginBranch(String loginBranch) {
        LoginBranch = loginBranch;
    }

    public String getLoginDate() {
        return LoginDate;
    }

    public void setLoginDate(String loginDate) {
        LoginDate = loginDate;
    }
}
