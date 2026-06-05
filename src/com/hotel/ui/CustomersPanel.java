package com.hotel.ui;

import com.hotel.model.Customer;
import com.hotel.service.HotelService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomersPanel extends JPanel {
    private HotelService service;
    private JTable table;
    private DefaultTableModel model;

    public CustomersPanel(HotelService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        model = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Email", "ID Proof"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("Add Customer");
        JButton searchBtn = new JButton("Search by Phone");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(searchBtn);
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showAddDialog());
        searchBtn.addActionListener(e -> searchByPhone());
        refreshBtn.addActionListener(e -> refresh());
    }

    public void refresh() {
        model.setRowCount(0);
        for (Customer c : service.getAllCustomers()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail(), c.getIdProof()});
        }
    }

    private void showAddDialog() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Add Customer", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameFld = new JTextField(15);
        JTextField emailFld = new JTextField(15);
        JTextField phoneFld = new JTextField(15);
        JTextField addrFld = new JTextField(15);
        JTextField idProofFld = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        dlg.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dlg.add(nameFld, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        dlg.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dlg.add(emailFld, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        dlg.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        dlg.add(phoneFld, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        dlg.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        dlg.add(addrFld, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        dlg.add(new JLabel("ID Proof:"), gbc);
        gbc.gridx = 1;
        dlg.add(idProofFld, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        dlg.add(btnPanel, gbc);

        saveBtn.addActionListener(e -> {
            int id = service.addCustomer(new Customer(
                nameFld.getText().trim(),
                emailFld.getText().trim(),
                phoneFld.getText().trim(),
                addrFld.getText().trim(),
                idProofFld.getText().trim()
            ));
            if (id > 0) {
                JOptionPane.showMessageDialog(dlg, "Customer added with ID: " + id);
                refresh();
                dlg.dispose();
            } else {
                JOptionPane.showMessageDialog(dlg, "Failed to add customer.");
            }
        });
        cancelBtn.addActionListener(e -> dlg.dispose());

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void searchByPhone() {
        String phone = JOptionPane.showInputDialog(this, "Enter phone number:");
        if (phone == null || phone.trim().isEmpty()) return;
        Customer c = service.getCustomerByPhone(phone.trim());
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Customer not found.");
        } else {
            JOptionPane.showMessageDialog(this,
                "ID: " + c.getId() + "\nName: " + c.getName() +
                "\nEmail: " + c.getEmail() + "\nPhone: " + c.getPhone() +
                "\nAddress: " + c.getAddress() + "\nID Proof: " + c.getIdProof());
        }
    }
}
