import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VisitorEntrySystem extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField purposeField = new JTextField();
    private final DefaultTableModel tableModel;
    private final JTable visitorTable;
    private final File dataFile = new File("visitor_entries.csv");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public VisitorEntrySystem() {
        setTitle("Visitor Entry Logging and Security Monitoring System");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel heading = new JLabel("VISITOR ENTRY LOGGING SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Visitor Details"));
        formPanel.add(new JLabel("Visitor Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Purpose of Visit:"));
        formPanel.add(purposeField);

        JButton submitButton = new JButton("Record Entry");
        JButton clearButton = new JButton("Clear Fields");
        formPanel.add(submitButton);
        formPanel.add(clearButton);

        tableModel = new DefaultTableModel(
                new String[]{"Visitor Name", "Purpose", "Entry Date & Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        visitorTable = new JTable(tableModel);
        visitorTable.setRowHeight(24);

        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(visitorTable), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Selected Entry");
        JButton exportButton = new JButton("Export CSV");
        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtons.add(deleteButton);
        bottomButtons.add(exportButton);
        centerPanel.add(bottomButtons, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        loadEntries();

        submitButton.addActionListener(e -> recordEntry());
        clearButton.addActionListener(e -> {
            nameField.setText("");
            purposeField.setText("");
            nameField.requestFocus();
        });
        deleteButton.addActionListener(e -> deleteSelectedEntry());
        exportButton.addActionListener(e -> exportEntries());

        setVisible(true);
    }

    private void recordEntry() {
        String name = nameField.getText().trim();
        String purpose = purposeField.getText().trim();

        if (name.isEmpty() || purpose.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both visitor name and purpose.",
                    "Missing Details", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String timestamp = LocalDateTime.now().format(TIME_FORMAT);
        tableModel.addRow(new Object[]{name, purpose, timestamp});
        saveEntries();

        JOptionPane.showMessageDialog(this,
                "Visitor entry recorded successfully!\nEntry Time: " + timestamp,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        nameField.setText("");
        purposeField.setText("");
        nameField.requestFocus();
    }

    private void deleteSelectedEntry() {
        int selectedRow = visitorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete the selected visitor entry?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            saveEntries();
        }
    }

    private void loadEntries() {
        if (!dataFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 3) {
                    tableModel.addRow(parts);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load saved entries: " + ex.getMessage());
        }
    }

    private void saveEntries() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.println(csvSafe(tableModel.getValueAt(i, 0).toString()) + ","
                        + csvSafe(tableModel.getValueAt(i, 1).toString()) + ","
                        + csvSafe(tableModel.getValueAt(i, 2).toString()));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save entries: " + ex.getMessage());
        }
    }

    private String csvSafe(String value) {
        // Basic CSV quoting for commas and quotation marks.
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private void exportEntries() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("visitor_report.csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File output = chooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(output))) {
                writer.println("Visitor Name,Purpose,Entry Date & Time");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.println(csvSafe(tableModel.getValueAt(i, 0).toString()) + ","
                            + csvSafe(tableModel.getValueAt(i, 1).toString()) + ","
                            + csvSafe(tableModel.getValueAt(i, 2).toString()));
                }
                JOptionPane.showMessageDialog(this, "Report exported to:\n" + output.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not export report: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VisitorEntrySystem::new);
    }
}
