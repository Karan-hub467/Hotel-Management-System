package com.hotel.ui;

import com.hotel.service.HotelService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private HotelService service = new HotelService();

    public MainFrame() {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel(service));
        tabs.addTab("Rooms", new RoomsPanel(service));
        tabs.addTab("Customers", new CustomersPanel(service));
        tabs.addTab("Bookings", new BookingsPanel(service));

        add(tabs);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
