package com.hotel.dao;

import com.hotel.model.Booking;
import com.hotel.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, c.name AS customer_name, r.room_number, r.room_type, r.price " +
            "FROM bookings b JOIN customers c ON b.customer_id = c.id " +
            "JOIN rooms r ON b.room_id = r.id ORDER BY b.check_in DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapBooking(rs));
        } catch (Exception e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
        return list;
    }

    public List<Booking> getActiveBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, c.name AS customer_name, r.room_number, r.room_type, r.price " +
            "FROM bookings b JOIN customers c ON b.customer_id = c.id " +
            "JOIN rooms r ON b.room_id = r.id " +
            "WHERE b.status IN ('confirmed','checked_in') ORDER BY b.check_in";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapBooking(rs));
        } catch (Exception e) {
            System.err.println("Error fetching active bookings: " + e.getMessage());
        }
        return list;
    }

    public Booking getById(int id) {
        String sql = "SELECT b.*, c.name AS customer_name, r.room_number, r.room_type, r.price " +
            "FROM bookings b JOIN customers c ON b.customer_id = c.id " +
            "JOIN rooms r ON b.room_id = r.id WHERE b.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapBooking(rs);
        } catch (Exception e) {
            System.err.println("Error fetching booking: " + e.getMessage());
        }
        return null;
    }

    public int insert(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id, room_id, check_in, check_out, status, total_amount) " +
            "VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getCustomerId());
            ps.setInt(2, booking.getRoomId());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setString(5, booking.getStatus());
            ps.setDouble(6, booking.getTotalAmount());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.err.println("Error inserting booking: " + e.getMessage());
        }
        return -1;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePaymentStatus(int id, String status) {
        String sql = "UPDATE bookings SET payment_status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            return false;
        }
    }

    private Booking mapBooking(ResultSet rs) throws Exception {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setRoomId(rs.getInt("room_id"));
        b.setCustomerName(rs.getString("customer_name"));
        b.setRoomNumber(rs.getString("room_number"));
        b.setRoomType(rs.getString("room_type"));
        b.setCheckIn(rs.getDate("check_in").toLocalDate());
        b.setCheckOut(rs.getDate("check_out").toLocalDate());
        b.setStatus(rs.getString("status"));
        b.setTotalAmount(rs.getDouble("total_amount"));
        b.setPaymentStatus(rs.getString("payment_status"));
        b.setRoomPrice(rs.getDouble("price"));
        return b;
    }
}
