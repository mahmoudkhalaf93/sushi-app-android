package com.example.zalpia.ui.home;

public class OfferModel {
    String timeText, offerNameLine1,offerNameLine2;
    int offerimage;
    Boolean isImageCalendar;

    public OfferModel(String timeText, String offerNameLine1, String offerNameLine2, int offerimage, Boolean isImageCalendar) {
        this.timeText = timeText;
        this.offerNameLine1 = offerNameLine1;
        this.offerNameLine2 = offerNameLine2;
        this.offerimage = offerimage;
        this.isImageCalendar = isImageCalendar;
    }

    public String getOfferNameLine2() {
        return offerNameLine2;
    }

    public void setOfferNameLine2(String offerNameLine2) {
        this.offerNameLine2 = offerNameLine2;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getOfferNameLine1() {
        return offerNameLine1;
    }

    public void setOfferNameLine1(String offerNameLine1) {
        this.offerNameLine1 = offerNameLine1;
    }

    public int getOfferimage() {
        return offerimage;
    }

    public void setOfferimage(int offerimage) {
        this.offerimage = offerimage;
    }

    public Boolean getIsImageCalendar() {
        return isImageCalendar;
    }

    public void setIsImageCalendar(Boolean isImageCalendar) {
        this.isImageCalendar = isImageCalendar;
    }
}
