package com.example.questionnairethesis;

public class Segment {
    String name;
    String emotion;
    public Segment(String name, String emotion) {
        this.name = name;
        this.emotion = emotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
}
