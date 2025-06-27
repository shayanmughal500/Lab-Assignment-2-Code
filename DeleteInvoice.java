package com.mycompany.submitinvoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Represents an invoice
class Invoice {
    private String studentId;
    private String feeType;
    private double amount;

    public Invoice(String studentId, String feeType, double amount) {
        this.studentId = studentId;
        this.feeType = feeType;
        this.amount = amount;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFeeType() {
        return feeType;
    }

    public double getAmount() {
        return amount;
    }
}

// Represents a student
class Student {
    private String studentId;

    public Student(String studentId) {
        this.studentId = studentId;
    }

    public Invoice createInvoice(String feeType, double amount) {
        return new Invoice(studentId, feeType, amount);
    }
}

// The main Fee System that processes invoices
class FeeSystem {
    private ArrayList<Invoice> invoiceDatabase = new ArrayList<>();

    public boolean submitInvoice(Invoice invoice) {
        if (validateInvoice(invoice)) {
            invoiceDatabase.add(invoice);
            return true;
        }
        return false;
    }

    private boolean validateInvoice(Invoice invoice) {
        return invoice.getAmount() > 0 && invoice.getFeeType() != null && !invoice.getFeeType().isEmpty();
    }

    public boolean deleteInvoice(String studentId, String feeType) {
        return invoiceDatabase.removeIf(inv -> inv.getStudentId().equals(studentId) && inv.getFeeType().equalsIgnoreCase(feeType));
    }

    public boolean updateInvoiceAmount(String studentId, String feeType, double newAmount) {
        for (Invoice inv : invoiceDatabase) {
            if (inv.getStudentId().equals(studentId) && inv.getFeeType().equalsIgnoreCase(feeType)) {
                invoiceDatabase.remove(inv);
                invoiceDatabase.add(new Invoice(studentId, feeType, newAmount));
                return true;
            }
        }
        return false;
    }

    public ArrayList<Invoice> getAllInvoices() {
        return invoiceDatabase;
    }
}

// Login window
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");

        loginButton.addActionListener(e -> authenticate());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        add(panel);
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Simple hardcoded credentials
        if ("admin".equals(username) && "password123".equals(password)) {
            SwingUtilities.invokeLater(() -> {
                SubmitInvoice submitInvoiceApp = new SubmitInvoice();
                submitInvoiceApp.setVisible(true);
            });
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Main GUI class
public class SubmitInvoice extends JFrame {

    private JTextField studentIdField;
    private JTextField feeTypeField;
    private JTextField amountField;
    private JButton submitButton, viewAllButton, deleteButton, updateButton;
    private FeeSystem feeSystem;

    public SubmitInvoice() {
        feeSystem = new FeeSystem();
        createUI();
    }

    private void createUI() {
        setTitle("Submit Invoice App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and fields
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdField = new JTextField(20);
        JLabel feeTypeLabel = new JLabel("Fee Type:");
        feeTypeField = new JTextField(20);
        JLabel amountLabel = new JLabel("Amount (PKR):");
        amountField = new JTextField(20);

        // Buttons
        submitButton = new JButton("Submit Invoice");
        viewAllButton = new JButton("View All Invoices");
        deleteButton = new JButton("Delete Invoice");
        updateButton = new JButton("Update Invoice");

        submitButton.addActionListener(e -> submitInvoice());
        viewAllButton.addActionListener(e -> viewAllInvoices());
        deleteButton.addActionListener(e -> deleteInvoice());
        updateButton.addActionListener(e -> updateInvoice());

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,5,5,5);
        panel.add(studentIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(feeTypeLabel, gbc);
        gbc.gridx = 1;
        panel.add(feeTypeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(amountLabel, gbc);
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void submitInvoice() {
        String studentId = studentIdField.getText().trim();
        String feeType = feeTypeField.getText().trim();
        String amountText = amountField.getText().trim();

        if (studentId.isEmpty() || feeType.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(studentId);
        Invoice invoice = student.createInvoice(feeType, amount);

        boolean success = feeSystem.submitInvoice(invoice);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "✅ Invoice submitted successfully!\n\n" +
                            "Student ID: " + studentId +
                            "\nFee Type: " + feeType +
                            "\nAmount: PKR " + amount,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Invalid invoice. Submission failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllInvoices() {
        ArrayList<Invoice> invoices = feeSystem.getAllInvoices();

        if (invoices.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No invoices submitted yet.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columns = {"Student ID", "Fee Type", "Amount (PKR)"};
        Object[][] data = new Object[invoices.size()][3];

        for (int i = 0; i < invoices.size(); i++) {
            Invoice inv = invoices.get(i);
            data[i][0] = inv.getStudentId();
            data[i][1] = inv.getFeeType();
            data[i][2] = String.format("%.2f", inv.getAmount());
        }

        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "All Submitted Invoices", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteInvoice() {
        String studentId = studentIdField.getText().trim();
        String feeType = feeTypeField.getText().trim();

        if (studentId.isEmpty() || feeType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student ID and Fee Type to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean deleted = feeSystem.deleteInvoice(studentId, feeType);

        if (deleted) {
            JOptionPane.showMessageDialog(this, "🗑️ Invoice deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "❌ No matching invoice found.", "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateInvoice() {
        String studentId = studentIdField.getText().trim();
        String feeType = feeTypeField.getText().trim();
        String amountText = amountField.getText().trim();

        if (studentId.isEmpty() || feeType.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields to update an invoice.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = feeSystem.updateInvoiceAmount(studentId, feeType, amount);

        if (updated) {
            JOptionPane.showMessageDialog(this, "✏️ Invoice updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "❌ No matching invoice found to update.", "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        feeTypeField.setText("");
        amountField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
