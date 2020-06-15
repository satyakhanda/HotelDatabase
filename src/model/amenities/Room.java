package model.amenities;

import java.util.Date;

public class Room {
    private int room_num;
    private Date date;
    private int rate;
    private int booking;

    public Room(int room_num, Date date, int rate, int booking) {
        this.booking = booking;
        this.date = date;
        this.rate = rate;
        this.room_num = room_num;
    }
}
