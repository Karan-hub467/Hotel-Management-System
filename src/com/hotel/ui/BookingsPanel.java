package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.service.HotelService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingsPanel extends JPanel {
    private HotelService service;
    private JTable table;
    private DefaultTableModel model;

    public BookingsPanel(HotelService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        model = new DefaultTableModel(new String[]{"ID", "Customer", "Room", "Check-In", "Check-Out",
            "Nights", "Total", "Status", "Payment"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton newBtn = new JButton("New Booking");
        JButton checkInBtn = new JButton("Check-In");
        JButton checkOutBtn = new JButton("Check-Out");
        JButton cancelBtn = new JButton("Cancel Booking");
        JButton refreshBtn = new JButton("Refresh");
        JButton allBtn = new JButton("Show All");

        btnPanel.add(newBtn);
        btnPanel.add(checkInBtn);
        btnPanel.add(checkOutBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(allBtn);
        add(btnPanel, BorderLayout.SOUTH);

        newBtn.addActionListener(e -> showNewBookingDialog());
        checkInBtn.addActionListener(e -> doCheckIn());
        checkOutBtn.addActionListener(e -> doCheckOut());
        cancelBtn.addActionListener(e -> doCancel());
        refreshBtn.addActionListener(e -> refresh());
        allBtn.addActionListener(e -> showAll());
    }

    public void refresh() {
        model.setRowCount(0);
        List<Booking> list = service.getActiveBookings();
        for (Booking b : list) addBookingRow(b);
    }

    private void showAll() {
        model.setRowCount(0);
        for (Booking b : service.getAllBookings()) addBookingRow(b);
    }

    private void addBookingRow(Booking b) {
        model.addRow(new Object[]{
            b.getId(), b.getCustomerName(), b.getRoomNumber(),
            b.getCheckIn(), b.getCheckOut(), b.getNights(),
            b.getTotalAmount(), b.getStatus(), b.getPaymentStatus()
        });
    }

    private void showNewBookingDialog() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "New Booking", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField custIdFld = new JTextField(10);
        JTextField ciFld = new JTextField(10);
        JTextField coFld = new JTextField(10);
        JComboBox<String> roomCombo = new JComboBox<>();
        roomCombo.setPreferredSize(new Dimension(250, 25));

        gbc.gridx = 0; gbc.gridy = 0;
        dlg.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        dlg.add(custIdFld, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dlg.add(new JLabel("Check-In (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        dlg.add(ciFld, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dlg.add(new JLabel("Check-Out (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        dlg.add(coFld, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dlg.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1;
        dlg.add(roomCombo, gbc);

        JButton availBtn = new JButton("Check Availability");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        dlg.add(availBtn, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Create Booking");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 5;
        dlg.add(btnPanel, gbc);

        saveBtn.setEnabled(false);

        availBtn.addActionListener(e -> {
            try {
                LocalDate ci = LocalDate.parse(ciFld.getText().trim());
                LocalDate co = LocalDate.parse(coFld.getText().trim());
                if (co.isBefore(ci) || co.isEqual(ci)) {
                    JOptionPane.showMessageDialog(dlg, "Check-out must be after check-in.");
                    return;
                }
                List<Room> avail = service.getAvailableRooms(ci, co);
                roomCombo.removeAllItems();
                if (avail.isEmpty()) {
                    JOptionPane.showMessageDialog(dlg, "No rooms available for those dates.");
                    saveBtn.setEnabled(false);
                } else {
                    for (Room r : avail) {
                        roomCombo.addItem(r.getId() + " - " + r.getRoomNumber() + " (" + r.getRoomType() + ") $" + r.getPrice() + "/night");
                    }
                    saveBtn.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Invalid date format.");
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                int custId = Integer.parseInt(custIdFld.getText().trim());
                LocalDate ci = LocalDate.parse(ciFld.getText().trim());
                LocalDate co = LocalDate.parse(coFld.getText().trim());
                String sel = (String) roomCombo.getSelectedItem();
                if (sel == null) return;
                int roomId = Integer.parseInt(sel.split(" - ")[0]);

                int bid = service.createBooking(custId, roomId, ci, co);
                if (bid > 0) {
                    JOptionPane.showMessageDialog(dlg, "Booking created! ID: " + bid);
                    refresh();
                    dlg.dispose();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Failed to create booking.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Invalid customer ID.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Error: " + ex.getMessage());
            }
        });
        cancelBtn.addActionListener(e -> dlg.dispose());

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private int getSelectedBookingId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a booking first.");
            return -1;
        }
        return (int) table.getValueAt(row, 0);
    }

    private void doCheckIn() {
        int id = getSelectedBookingId();
        if (id < 0) return;
        if (service.checkIn(id)) {
            JOptionPane.showMessageDialog(this, "Check-In successful!");
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Check-In failed.");
        }
    }

    private void doCheckOut() {
        int id = getSelectedBookingId();
        if (id < 0) return;
        Booking b = service.getBookingById(id);
        if (b == null) { JOptionPane.showMessageDialog(this, "Booking not found."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Total: $" + b.getTotalAmount() + "\nMark as paid and check out?",
            "Confirm Check-Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && service.checkOut(id)) {
            JOptionPane.showMessageDialog(this, "Check-Out successful!");
            refresh();
        }
    }

    private void doCancel() {
        int id = getSelectedBookingId();
        if (id < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel this booking?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && service.cancelBooking(id)) {
            JOptionPane.showMessageDialog(this, "Booking cancelled.");
            refresh();
        }
    }
}
