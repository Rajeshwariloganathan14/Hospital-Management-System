/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.hospitalmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HospitalManagementSystem extends JFrame {
    private JTextField idField, nameField, diseaseField, genderField, dateField;
    private JTable table;
    private DefaultTableModel tableModel;

    public HospitalManagementSystem() {
        setTitle("Hospital Management System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel - input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Patient ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Disease:"));
        diseaseField = new JTextField();
        inputPanel.add(diseaseField);

        inputPanel.add(new JLabel("Gender:"));
        genderField = new JTextField();
        inputPanel.add(genderField);

        inputPanel.add(new JLabel("Admit Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        JButton addButton = new JButton("Add Patient");
        inputPanel.add(addButton);
        add(inputPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Disease", "Gender", "Admit Date"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom button
        JButton viewButton = new JButton("View All Patients");
        add(viewButton, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addPatient());
        viewButton.addActionListener(e -> loadPatients());

        setVisible(true);
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/hospital_db";
        String user = "root";
        String password = "password"; // Change this if your MySQL has a password
        return DriverManager.getConnection(url, user, password);
    }

    private void addPatient() {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO patients (id, name, disease, gender, admit_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(idField.getText()));
            stmt.setString(2, nameField.getText());
            stmt.setString(3, diseaseField.getText());
            stmt.setString(4, genderField.getText());
            stmt.setDate(5, Date.valueOf(dateField.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadPatients() {
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM patients";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Clear table
            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("disease"),
                    rs.getString("gender"),
                    rs.getDate("admit_date")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HospitalManagementSystem::new);
    }
}
