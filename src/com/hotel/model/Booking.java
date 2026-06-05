package com.hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private int id;
    private int customerId;
    private int roomId;
    private String customerName;
    private String roomNumber;
    private String roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;
    private double totalAmount;
    private String paymentStatus;
    private double roomPrice;

    public Booking() {}

    public Booking(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) {
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = "confirmed";
        this.paymentStatus = "pending";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public double getRoomPrice() { return roomPrice; }
    public void setRoomPrice(double roomPrice) { this.roomPrice = roomPrice; }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    @Override
    public String toString() {
        return String.format("#%d | %s | Room %s | %s to %s | %d nights | $%.2f | %s",
            id, customerName, roomNumber, checkIn, checkOut, getNights(), totalAmount, status);
    }
}
