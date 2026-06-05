package com.hotel.model;

public class Room {
    private int id;
    private String roomNumber;
    private String roomType;
    private double price;
    private int capacity;
    private String status;
    private String description;

    public Room() {}

    public Room(String roomNumber, String roomType, double price, int capacity, String description) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.status = "available";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("%-6s %-10s $%-8.2f Cap:%d [%s]",
            roomNumber, roomType, price, capacity, status);
    }
}
