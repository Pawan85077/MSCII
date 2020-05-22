package com.codinghelper.mscii;

public class studentDetail {
    public String username,Examrall,session,Email,phoneno,gender,AccountType,Scourse,userstatus,imageUrl;

    public studentDetail(){

    }

    public studentDetail(String username, String examrall, String session, String email, String phoneno, String gender,String AccountType,String course,String userstatus,String imageUrl) {
        this.username = username;
        Examrall = examrall;
        this.session = session;
        Email = email;
        this.phoneno = phoneno;
        this.gender = gender;
        Scourse=course;
        this.AccountType=AccountType;
        this.userstatus=userstatus;
        this.imageUrl=imageUrl;
    }
}
