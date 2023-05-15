package com.example.nofoodsharingproject.models;

public class Faq {
    private String question;
    private String answer;
    private boolean isShow = false;

    public Faq() {}

    public Faq(String question, String answer) {
        this.answer = answer;
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
