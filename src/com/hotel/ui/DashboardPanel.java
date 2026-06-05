package com.hotel.ui;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.service.HotelService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private HotelService service;
    private JLabel totalRoomsLbl, occupiedLbl, availableLbl, rateLbl, activeBookingsLbl;
    private JTextArea recentArea;

    public DashboardPanel(HotelService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        cardsPanel.setPreferredSize(new Dimension(0, 120));

        totalRoomsLbl = createCard(cardsPanel, "Total Rooms", "");
        occupiedLbl = createCard(cardsPanel, "Occupied", "");
        availableLbl = createCard(cardsPanel, "Available", "");
        rateLbl = createCard(cardsPanel, "Occupancy Rate", "");
        activeBookingsLbl = createCard(cardsPanel, "Active Bookings", "");

        add(cardsPanel, BorderLayout.NORTH);

        recentArea = new JTextArea();
        recentArea.setEditable(false);
        recentArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(recentArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Active Bookings"));

        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private JLabel createCard(JPanel parent, String title, String value) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(240, 248, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 215, 230)),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        titleLbl.setForeground(Color.GRAY);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Arial", Font.BOLD, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(titleLbl, gbc);
        gbc.gridy = 1;
        card.add(valLbl, gbc);

        parent.add(card);
        return valLbl;
    }

    public void refresh() {
        List<Room> rooms = service.getAllRooms();
        List<Booking> active = service.getActiveBookings();

        int total = rooms.size();
        int occupied = (int) rooms.stream().filter(r -> r.getStatus().equals("occupied") || r.getStatus().equals("booked")).count();

        totalRoomsLbl.setText(String.valueOf(total));
        occupiedLbl.setText(String.valueOf(occupied));
        availableLbl.setText(String.valueOf(total - occupied));
        rateLbl.setText(total > 0 ? (occupied * 100 / total) + "%" : "0%");
        activeBookingsLbl.setText(String.valueOf(active.size()));

        StringBuilder sb = new StringBuilder();
        if (active.isEmpty()) {
            sb.append("No active bookings right now.");
        } else {
            for (Booking b : active) {
                sb.append(String.format(" #%d | %-20s | Room %-5s | %s to %s | $%.2f%n",
                    b.getId(), b.getCustomerName(), b.getRoomNumber(),
                    b.getCheckIn(), b.getCheckOut(), b.getTotalAmount()));
            }
        }
        recentArea.setText(sb.toString());
        recentArea.setCaretPosition(0);
    }
}
