package com.panca_nugraha.baschedulejo;

public class Agenda {
    private String id;
    private String agenda;
    private String date;
    private String time;
    private String location;

    public Agenda(){

    }
    public Agenda(String agenda, String date, String time, String location) {
        this.agenda = agenda;
        this.date = date;
        this.time = time;
        this.location = location;
    }
    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
