package com.codinghelper.mscii;

public class studentDetail {
    public String Username,Examrall,session,Email,phoneno,gender,AccountType;

    public studentDetail(){

    }

    public studentDetail(String username, String examrall, String session, String email, String phoneno, String gender,String AccountType) {
        Username = username;
        Examrall = examrall;
        this.session = session;
        Email = email;
        this.phoneno = phoneno;
        this.gender = gender;
        this.AccountType=AccountType;
    }
}
