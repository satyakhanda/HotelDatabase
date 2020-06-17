package model.amenities;

import java.util.Date;

public class Room {
    private int room_num;
    private String date;
    private int rate;
    private int booking;

    public Room(int room_num, String date, int rate, int booking) {
        this.booking = booking;
        this.date = date;
        this.rate = rate;
        this.room_num = room_num;
    }

    public int getRoom_num() {
        return room_num;
    }

    public String getDate() {
        return date;
    }

    public int getBooking() {
        return booking;
    }

    public int getRate() {
        return rate;
    }
}
