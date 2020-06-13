package model.amenities;

import java.util.Date;

public class Booking {
    private int bookingID;
    private Date startDate;
    private Date endDate;
    private int sizeOfParty;

    public Booking(int bookingID, Date startDate, Date endDate, int sizeOfParty) {
        this.bookingID = bookingID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sizeOfParty = sizeOfParty;
    }
}
