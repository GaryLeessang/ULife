package com.example.joe.cityumobile.DataModel.BmobModel;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class RegisterRequest extends BmobObject {

    private String studentID;
    private String studentName;
    private String email;
    private String state;
    private BmobFile cardPhoto;

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BmobFile getCardPhoto() {
        return cardPhoto;
    }

    public void setCardPhoto(BmobFile cardPhoto) {
        this.cardPhoto = cardPhoto;
    }


}
