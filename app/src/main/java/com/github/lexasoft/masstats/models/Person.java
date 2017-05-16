package com.github.lexasoft.masstats.models;

public class Person {

    private int id;
    private String name;
    private int coincidences;

    public Person() {

    }

    public Person(String name, int coincidences) {
        this.name = name;
        this.coincidences = coincidences;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getCoincidences() {
        return coincidences;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoincidences(int coincidences) {
        this.coincidences = coincidences;
    }

    @Override
    public String toString() {
        return name;
    }
}
