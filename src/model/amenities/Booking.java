package model.amenities;

import java.util.Date;

public class Booking {
    private int bookingID;
    private String startDate;
    private String endDate;
    private int sizeOfParty;

    public Booking(int bookingID, String startDate, String endDate, int sizeOfParty) {
        this.bookingID = bookingID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sizeOfParty = sizeOfParty;
    }

    public int getBookingID() {
        return this.bookingID;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public int getSizeOfParty() {
        return this.sizeOfParty;
    }
}
