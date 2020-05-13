package com.example.mystilib;

public class Book {

    public String name; //Le nom du livre
    public String group; //Le groupe auquel appartient le livre
    public Integer state; //L'état, 0 = Acquis, 1 = Emprunté, 2 = A Acheter
    public Integer indicator; //L'indicateur, 0 = A lire, 1 = En cours, 2 = Lu
    public Float stars; //La note sur 5
    public String note; //Commentaire

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public Integer getState() {
        return state;
    }

    public Integer getIndicator() {
        return indicator;
    }

    public Float getStars() {
        return stars;
    }

    public String getNote() {
        return note;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setIndicator(Integer indicator) {
        this.indicator = indicator;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Book(String name, String group, int state, int indicator, float stars) {
        this.name = name;
        this.group = group;
        this.state = state;
        this.indicator = indicator;
        this.stars = stars;
    }
}
