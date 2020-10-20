package com.codinghelper.mscii;

public class Friends {
   public String username,imageUrl,userstatus;

   public Friends(){

   }

    public Friends(String username, String imageUrl, String userstatus) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.userstatus = userstatus;

    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }
/*  public Friends(String Username, String imageUrl, String Userstatus) {
        this.Username = Username;
        this.imageUrl = imageUrl;
        this.Userstatus = Userstatus;
    }

    public String getUsernme() {
        return Username;
    }

    public void setUsernme(String username) {
        Username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserstats() {
        return Userstatus;
    }

    public void setUserstats(String userstatus) {
        Userstatus = userstatus;
    }*/
}
