package com.hotel.dao;

import com.hotel.model.Room;
import com.hotel.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRoom(rs));
        } catch (Exception e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
        return list;
    }

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'available' AND id NOT IN (" +
            "SELECT room_id FROM bookings WHERE status IN ('confirmed','checked_in') " +
            "AND (check_in < ? AND check_out > ?)) ORDER BY room_number";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(checkOut));
            ps.setDate(2, Date.valueOf(checkIn));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRoom(rs));
            rs.close();
        } catch (Exception e) {
            System.err.println("Error fetching available rooms: " + e.getMessage());
        }
        return list;
    }

    public Room getById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRoom(rs);
        } catch (Exception e) {
            System.err.println("Error fetching room: " + e.getMessage());
        }
        return null;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error updating room status: " + e.getMessage());
            return false;
        }
    }

    public int insert(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price, capacity, description) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getDescription());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.err.println("Error inserting room: " + e.getMessage());
        }
        return -1;
    }

    private Room mapRoom(ResultSet rs) throws Exception {
        Room r = new Room();
        r.setId(rs.getInt("id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setPrice(rs.getDouble("price"));
        r.setCapacity(rs.getInt("capacity"));
        r.setStatus(rs.getString("status"));
        r.setDescription(rs.getString("description"));
        return r;
    }
}
