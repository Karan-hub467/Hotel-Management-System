package com.hotel.ui;

import com.hotel.model.Room;
import com.hotel.service.HotelService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomsPanel extends JPanel {
    private HotelService service;
    private JTable table;
    private DefaultTableModel model;

    public RoomsPanel(HotelService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        model = new DefaultTableModel(new String[]{"ID", "Room No", "Type", "Price", "Capacity", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("Add Room");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showAddDialog());
        refreshBtn.addActionListener(e -> refresh());
    }

    public void refresh() {
        model.setRowCount(0);
        for (Room r : service.getAllRooms()) {
            model.addRow(new Object[]{r.getId(), r.getRoomNumber(),
                r.getRoomType(), r.getPrice(), r.getCapacity(), r.getStatus()});
        }
    }

    private void showAddDialog() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Add Room", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField roomNoFld = new JTextField(15);
        JTextField typeFld = new JTextField(15);
        JTextField priceFld = new JTextField(15);
        JTextField capFld = new JTextField(15);
        JTextField descFld = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        dlg.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        dlg.add(roomNoFld, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        dlg.add(new JLabel("Type (single/double/suite/deluxe):"), gbc);
        gbc.gridx = 1;
        dlg.add(typeFld, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        dlg.add(new JLabel("Price per night:"), gbc);
        gbc.gridx = 1;
        dlg.add(priceFld, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        dlg.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        dlg.add(capFld, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        dlg.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        dlg.add(descFld, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        dlg.add(btnPanel, gbc);

        saveBtn.addActionListener(e -> {
            try {
                Room r = new Room(
                    roomNoFld.getText().trim(),
                    typeFld.getText().trim().toLowerCase(),
                    Double.parseDouble(priceFld.getText().trim()),
                    Integer.parseInt(capFld.getText().trim()),
                    descFld.getText().trim()
                );
                if (service.addRoom(r)) {
                    JOptionPane.showMessageDialog(dlg, "Room added!");
                    refresh();
                    dlg.dispose();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Failed to add room.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Invalid number format.");
            }
        });
        cancelBtn.addActionListener(e -> dlg.dispose());

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }
}
