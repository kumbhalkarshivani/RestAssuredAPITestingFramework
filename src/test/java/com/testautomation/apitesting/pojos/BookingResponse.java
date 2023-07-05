package com.testautomation.apitesting.pojos;

public class BookingResponse {
    private Booking booking;
    private int bookingid;

    public BookingResponse() {

    }

    public BookingResponse(int bookingid,Booking booking) {
        setBookingid(bookingid);
        setBooking(booking);
    }
    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

}
