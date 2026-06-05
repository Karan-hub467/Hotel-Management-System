package com.hotel.service;

import com.hotel.dao.*;
import com.hotel.model.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HotelService {
    private RoomDAO roomDAO = new RoomDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    public List<Room> getAllRooms() { return roomDAO.getAllRooms(); }
    public Room getRoomById(int id) { return roomDAO.getById(id); }

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        return roomDAO.getAvailableRooms(checkIn, checkOut);
    }

    public boolean addRoom(Room room) {
        return roomDAO.insert(room) > 0;
    }

    public List<Customer> getAllCustomers() { return customerDAO.getAllCustomers(); }
    public Customer getCustomerById(int id) { return customerDAO.getById(id); }
    public Customer getCustomerByPhone(String phone) { return customerDAO.getByPhone(phone); }

    public int addCustomer(Customer customer) {
        return customerDAO.insert(customer);
    }

    public List<Booking> getAllBookings() { return bookingDAO.getAllBookings(); }
    public List<Booking> getActiveBookings() { return bookingDAO.getActiveBookings(); }
    public Booking getBookingById(int id) { return bookingDAO.getById(id); }

    public int createBooking(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) {
        Room room = roomDAO.getById(roomId);
        if (room == null) return -1;

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double total = room.getPrice() * nights;

        Booking booking = new Booking(customerId, roomId, checkIn, checkOut);
        booking.setTotalAmount(total);

        int bookingId = bookingDAO.insert(booking);
        if (bookingId > 0) {
            roomDAO.updateStatus(roomId, "booked");
        }
        return bookingId;
    }

    public boolean checkIn(int bookingId) {
        boolean ok = bookingDAO.updateStatus(bookingId, "checked_in");
        if (ok) {
            Booking b = bookingDAO.getById(bookingId);
            if (b != null) roomDAO.updateStatus(b.getRoomId(), "occupied");
        }
        return ok;
    }

    public boolean checkOut(int bookingId) {
        Booking b = bookingDAO.getById(bookingId);
        if (b == null) return false;
        bookingDAO.updatePaymentStatus(bookingId, "paid");
        boolean ok = bookingDAO.updateStatus(bookingId, "checked_out");
        if (ok) roomDAO.updateStatus(b.getRoomId(), "available");
        return ok;
    }

    public boolean cancelBooking(int bookingId) {
        Booking b = bookingDAO.getById(bookingId);
        if (b == null) return false;
        boolean ok = bookingDAO.updateStatus(bookingId, "cancelled");
        if (ok) roomDAO.updateStatus(b.getRoomId(), "available");
        return ok;
    }
}
