package com.hotel.dao;

import com.hotel.model.Customer;
import com.hotel.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapCustomer(rs));
        } catch (Exception e) {
            System.err.println("Error fetching customers: " + e.getMessage());
        }
        return list;
    }

    public Customer getById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapCustomer(rs);
        } catch (Exception e) {
            System.err.println("Error fetching customer: " + e.getMessage());
        }
        return null;
    }

    public Customer getByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapCustomer(rs);
        } catch (Exception e) {
            System.err.println("Error fetching customer: " + e.getMessage());
        }
        return null;
    }

    public int insert(Customer customer) {
        String sql = "INSERT INTO customers (name, email, phone, address, id_proof) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getIdProof());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.err.println("Error inserting customer: " + e.getMessage());
        }
        return -1;
    }

    public boolean update(int id, Customer customer) {
        String sql = "UPDATE customers SET name=?, email=?, phone=?, address=?, id_proof=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getIdProof());
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    private Customer mapCustomer(ResultSet rs) throws Exception {
        Customer c = new Customer();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setIdProof(rs.getString("id_proof"));
        return c;
    }
}
