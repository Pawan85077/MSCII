package com.codinghelper.mscii;

public class studentDetail {
    public String username,Examrall,session,Email,phoneno,gender,AccountType,Scourse,userstatus,imageUrl,TeacherNotes,StudentNotes,FavNotes,userSong,states1,states2;
    public Integer countQ,countA,TotalLikes;
    public studentDetail(){

    }

    public studentDetail(String username, String examrall, String session, String email, String phoneno, String gender,String AccountType,String course,
                         String userstatus,String imageUrl,String TeacherNotes,String StudentNotes,String FavNotes,String userSong,Integer countQ,Integer countA,Integer TotalLikes,String states1, String states2) {
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
        this.TeacherNotes=TeacherNotes;
        this.StudentNotes=StudentNotes;
        this.FavNotes=FavNotes;
        this.userSong=userSong;
        this.countQ=countQ;
        this.countA=countA;
        this.TotalLikes=TotalLikes;
        this.states1=states1;
        this.states2=states2;
    }
}
