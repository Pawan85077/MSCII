package com.codinghelper.mscii;

import com.google.firebase.database.DatabaseReference;

public class StudentQuestion {
    private String QuestionAsked,Answer,AskerImage,AnswererImage;

    public StudentQuestion() {
    }

    public StudentQuestion(String QuestionAsked,String Answer,String AskerImage,String AnswererImage) {
        this.QuestionAsked = QuestionAsked;
        this.Answer=Answer;
        this.AskerImage=AskerImage;
        this.AnswererImage=AnswererImage;
    }

    public String getQuestionAsked() {
        return QuestionAsked;
    }
    public String getAnswer() {
        return Answer;
    }
    public String getAskerImage() {
        return AskerImage;
    }
    public String getAnswererImage() {
        return AnswererImage;
    }


}
