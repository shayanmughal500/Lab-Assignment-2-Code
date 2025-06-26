package deleteinvoice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class DeleteInvoice extends JFrame {

    private JTextField invoiceIdField;
    private JTextField invoiceDescField;
    private JButton addButton;
    private JButton deleteButton;
    private JLabel statusLabel;

    // In-memory storage of invoices: invoice ID -> description
    private Map<Integer, String> invoices = new HashMap<>();

    public DeleteInvoice() {
        setTitle("Invoice Manager");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JLabel idLabel = new JLabel("Invoice ID:");
        invoiceIdField = new JTextField(10);

        JLabel descLabel = new JLabel("Description:");
        invoiceDescField = new JTextField(20);

        addButton = new JButton("Add Invoice");
        deleteButton = new JButton("Delete Invoice");

        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Invoice ID label + field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,10,5,5);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(invoiceIdField, gbc);

        // Description label + field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(invoiceDescField, gbc);

        // Add Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        // Delete Button
        gbc.gridx = 1;
        panel.add(deleteButton, gbc);

        // Status label spanning 2 columns
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,10,10,10);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(statusLabel, gbc);

        add(panel);

        // Button listeners
        addButton.addActionListener(e -> addInvoice());
        deleteButton.addActionListener(e -> deleteInvoice());
    }

    private void addInvoice() {
        String idText = invoiceIdField.getText().trim();
        String desc = invoiceDescField.getText().trim();

        if (idText.isEmpty() || desc.isEmpty()) {
            showError("Invoice ID and Description cannot be empty.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showError("Invoice ID must be a valid integer.");
            return;
        }

        if (invoices.containsKey(id)) {
            showError("Invoice ID " + id + " already exists.");
            return;
        }

        invoices.put(id, desc);
        showSuccess("Invoice ID " + id + " added.");
        clearFields();
    }

    private void deleteInvoice() {
        String idText = invoiceIdField.getText().trim();

        if (idText.isEmpty()) {
            showError("Enter Invoice ID to delete.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showError("Invoice ID must be a valid integer.");
            return;
        }

        if (invoices.remove(id) != null) {
            showSuccess("Invoice ID " + id + " deleted.");
        } else {
            showError("Invoice ID " + id + " not found.");
        }
        clearFields();
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }

    private void showSuccess(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(new Color(0, 128, 0));  // Dark green
    }

    private void clearFields() {
        invoiceIdField.setText("");
        invoiceDescField.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            DeleteInvoice frame = new DeleteInvoice();
            frame.setVisible(true);
        });
    }
}
