package com.panca_nugraha.baschedulejo;

public class JSONEvent {
    private String agenda;
    private String date;
    private String location;
    private String time;

    // Konstruktor kosong (default)
    public JSONEvent() {
        // Diperlukan konstruktor kosong untuk Firebase
    }

    // Konstruktor dengan parameter
    public JSONEvent(String agenda, String date, String location, String time) {
        this.agenda = agenda;
        this.date = date;
        this.location = location;
        this.time = time;
    }

    // Getter dan setter (sesuaikan kebutuhan)
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
