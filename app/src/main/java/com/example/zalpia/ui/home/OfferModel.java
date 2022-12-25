package com.example.zalpia.ui.home;

import org.jetbrains.annotations.NotNull;

public class OfferModel {
    String time, nameline1, nameline2,timeAr, nameline1Ar, nameline2Ar, image,firebaseId;

    Boolean isTimeCalendar;
    public OfferModel(){

    }

    public String getTimeAr() {
        return timeAr;
    }

    public void setTimeAr(String timeAr) {
        this.timeAr = timeAr;
    }

    public String getNameline1Ar() {
        return nameline1Ar;
    }

    public void setNameline1Ar(String nameline1Ar) {
        this.nameline1Ar = nameline1Ar;
    }

    public String getNameline2Ar() {
        return nameline2Ar;
    }

    public void setNameline2Ar(String nameline2Ar) {
        this.nameline2Ar = nameline2Ar;
    }

    public String getNameline2() {
        return nameline2;
    }

    public void setNameline2(String nameline2) {
        this.nameline2 = nameline2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNameline1() {
        return nameline1;
    }

    public void setNameline1(String nameline1) {
        this.nameline1 = nameline1;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getIsTimeCalendar() {
        return isTimeCalendar;
    }

    public void setIsTimeCalendar(Boolean isTimeCalendar) {
        this.isTimeCalendar = isTimeCalendar;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public Boolean getTimeOrCalendar() {
        return isTimeCalendar;
    }

    public void setTimeOrCalendar(Boolean timeOrCalendar) {
        isTimeCalendar = timeOrCalendar;
    }

    @NotNull
    @Override
    public String toString() {
        return "OfferModel{" +
                "timeText='" + time + '\'' +
                ", offerNameLine1='" + nameline1 + '\'' +
                ", offerNameLine2='" + nameline2 + '\'' +
                ", offerimage=" + image +
                ", isTimeOrCalendar=" + isTimeCalendar +
                '}';
    }
}
