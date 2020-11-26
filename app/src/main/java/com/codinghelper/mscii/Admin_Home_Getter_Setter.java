package com.codinghelper.mscii;

public class Admin_Home_Getter_Setter {
    public String senderImage, senderDepartmentName, receiverName, messageTitle, messageBody, likeCount, timeOfPost, dateOfPost;

    public Admin_Home_Getter_Setter(){

    }

    public Admin_Home_Getter_Setter(String senderImage, String senderDepartmentName, String receiverName, String messageTitle, String messageBody, String likeCount, String timeOfPost, String dateOfPost) {
        this.senderImage = senderImage;
        this.senderDepartmentName = senderDepartmentName;
        this.receiverName = receiverName;
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;
        this.likeCount = likeCount;
        this.timeOfPost = timeOfPost;
        this.dateOfPost = dateOfPost;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getSenderDepartmentName() {
        return senderDepartmentName;
    }

    public void setSenderDepartmentName(String senderDepartmentName) {
        this.senderDepartmentName = senderDepartmentName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(String timeOfPost) {
        this.timeOfPost = timeOfPost;
    }

    public String getDateOfPost() {
        return dateOfPost;
    }

    public void setDateOfPost(String dateOfPost) {
        this.dateOfPost = dateOfPost;
    }
}
