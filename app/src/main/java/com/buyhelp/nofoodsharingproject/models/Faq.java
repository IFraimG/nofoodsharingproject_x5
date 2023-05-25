package com.buyhelp.nofoodsharingproject.models;

import android.graphics.drawable.Drawable;

public class Faq {
    private String question;
    private String answer;
    private boolean isShow = false;
    private int[] images;

    public Faq() {}

    public Faq(String question, String answer) {
        this.answer = answer;
        this.question = question;
    }

    public Faq(String question, String answer, int[] images) {
        this.answer = answer;
        this.question = question;
        this.images = images;
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

    public void setImages(int[] images) {
        this.images = images;
    }

    public int[] getImages() {
        return images;
    }
}
