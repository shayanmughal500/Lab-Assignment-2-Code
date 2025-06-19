package deleteinvoice;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DeleteInvoice extends JFrame {

    // Database connection parameters — change as per your setup
    private static final String DB_URL = "jdbc:mysql://localhost:3306/accounting_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private JTextField invoiceIdField;
    private JButton deleteButton;
    private JLabel statusLabel;

    public DeleteInvoice() {
        setTitle("Delete Invoice");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window

        initComponents();
    }

    private void initComponents() {
        // Create components
        JLabel label = new JLabel("Enter Invoice ID to delete:");
        invoiceIdField = new JTextField(15);
        deleteButton = new JButton("Delete Invoice");
        statusLabel = new JLabel(" ");

        // Set layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        // Text field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(invoiceIdField, gbc);

        // Button
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(deleteButton, gbc);

        // Status label (full width)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(statusLabel, gbc);

        add(panel);

        // Add action listener to the delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteInvoiceAction();
            }
        });
    }

    private void deleteInvoiceAction() {
        String input = invoiceIdField.getText().trim();
        if (input.isEmpty()) {
            statusLabel.setText("Please enter an invoice ID.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        int invoiceId;
        try {
            invoiceId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            statusLabel.setText("Invoice ID must be a valid integer.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        boolean deleted = deleteInvoiceById(invoiceId);
        if (deleted) {
            statusLabel.setText("Invoice with ID " + invoiceId + " deleted successfully.");
            statusLabel.setForeground(new Color(0, 128, 0));  // dark green
        } else {
            statusLabel.setText("Invoice with ID " + invoiceId + " not found or could not be deleted.");
            statusLabel.setForeground(Color.RED);
        }
    }

    private boolean deleteInvoiceById(int invoiceId) {
        String deleteSQL = "DELETE FROM invoices WHERE invoice_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, invoiceId);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting invoice: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        // Optional: Set system look and feel for better GUI appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            DeleteInvoice frame = new DeleteInvoice();
            frame.setVisible(true);
        });
    }
}
