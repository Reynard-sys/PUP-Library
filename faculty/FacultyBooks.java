import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.plaf.basic.BasicSliderUI;

public class FacultyBooks {
    public static void facBooks(JFrame faculty) {
        JFrame facultyBooks = new JFrame();
        facultyBooks.getContentPane().setBackground(Color.decode("#f4f6f9"));
        facultyBooks.setTitle("PUP Library System/faculty/Dashboard/");
        facultyBooks.setSize(600, 600);
        facultyBooks.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        facultyBooks.setResizable(false);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        headerPanel.setPreferredSize(new Dimension(facultyBooks.getWidth(), 70));

        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH)));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));

        JLabel title = new JLabel("PUP LIBRARY");
        title.setFont(new Font("Roboto", Font.PLAIN, 23));
        title.setForeground(Color.decode("#800201"));
        title.setBorder(BorderFactory.createEmptyBorder(20, 4, 20, 10));

        JButton dropDown = new JButton(new ImageIcon(new ImageIcon("assets/dropdownBlack.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        dropDown.setFocusPainted(false);
        dropDown.setBorderPainted(false);
        dropDown.setContentAreaFilled(false);
        dropDown.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 35));

        JPopupMenu menu = new JPopupMenu();
        menu.setPreferredSize(new Dimension(170, 60));
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.decode("#800201")));

        JMenuItem notif = new JMenuItem("Notification", new ImageIcon(new ImageIcon("assets/notification.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        JMenuItem signOut = new JMenuItem("Sign out", new ImageIcon(new ImageIcon("assets/logout.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));

        signOut.addActionListener(e -> {
            facultyBooks.dispose();
            SwingUtilities.invokeLater(() -> Main.main(new String[]{}));
        });

        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(170, 1));
        separator.setBackground(Color.BLACK);
        menu.add(notif);
        menu.add(separator);
        menu.add(signOut);

        dropDown.addActionListener(e -> menu.show(dropDown, -145, dropDown.getHeight() - 20));

        JPanel facultyNav = new JPanel(new GridLayout(1, 4));
        facultyNav.setBounds(0, 0, facultyBooks.getWidth(), 53);
        facultyNav.setBackground(Color.BLACK);

        String[] buttonNames = {"Books", "Library Pass", "Log"};
        for (String name : buttonNames) {
            JButton btn = new JButton(name);
            btn.setBackground(Color.decode("#800201"));
            btn.setForeground(name.equals("Books") ? Color.YELLOW : Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addActionListener(e -> {
                if (name.equals("Books"))
                {
                    return;
                }
                else{
                    facultyBooks.setVisible(false);
                    if (name.equals("Log")) FacultyLog.facLog(facultyBooks);
                    else if (name.equals("Library Pass")) FacultyPass.facPass(facultyBooks);
                }
                
            });

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.decode("#800201"));
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));
            panel.add(btn, BorderLayout.CENTER);
            facultyNav.add(panel);
        }

        String[] columns = {"Book Title", "Year", "Author", "ISBN", "Copies", "Category", "Delete"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try (Connection con = DBConnection.connect();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM book ORDER BY book_title ASC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("book_title"),
                    rs.getInt("publication_year"),
                    rs.getString("book_author"),
                    rs.getString("isbn_number"),
                    rs.getInt("total_copies"),
                    rs.getString("book_category"),
                    "Delete"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), model, table));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(center);
        table.getColumnModel().getColumn(5).setCellRenderer(center);

        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);

        JTextField searchField = new JTextField();
        JComboBox<String> categoryFilter = new JComboBox<>(new String[]{"All", "English", "Filipino", "Science", "Math"});

        Runnable filterAndSort = () -> {
            String searchText = searchField.getText().trim().toLowerCase();
            String selectedCategory = ((String) categoryFilter.getSelectedItem()).toLowerCase();
            RowFilter<DefaultTableModel, Object> rf = new RowFilter<>() {
                public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    String title = entry.getStringValue(0).toLowerCase();
                    String category = entry.getStringValue(5).toLowerCase();
                    return title.contains(searchText) && (selectedCategory.equals("all") || category.equals(selectedCategory));
                }
            };
            sorter.setRowFilter(rf);
        };

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterAndSort.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterAndSort.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterAndSort.run(); }
        });

        categoryFilter.addActionListener(e -> filterAndSort.run());

        JPanel searchFilterPanel = new JPanel(new BorderLayout(10, 0));
        searchFilterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchFilterPanel.setBackground(Color.decode("#f4f6f9"));
        searchFilterPanel.add(searchField, BorderLayout.CENTER);
        searchFilterPanel.add(categoryFilter, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(565, 385));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBounds(9, 130, 565, 330);
        contentPanel.setBackground(Color.decode("#ebebeb"));
        contentPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        ImageIcon addIcon = new ImageIcon(new ImageIcon("assets/add_button.png").getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        JButton addButton = new JButton(addIcon);
        addButton.setBounds(480, 70, 35, 50);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setToolTipText("Add Book");
        addButton.addActionListener(e -> {
            facultyBooks.setVisible(false);
            BookInfo.bookInfo(facultyBooks);
        });

        JPanel mainPanel = new JPanel(null);
        mainPanel.setSize(new Dimension(facultyBooks.getWidth(), 500));
        mainPanel.setBackground(Color.decode("#f4f6f9"));
        searchFilterPanel.setBounds(120, 70, 350, 50);
        mainPanel.add(addButton);
        mainPanel.add(facultyNav);
        mainPanel.add(searchFilterPanel);
        mainPanel.add(contentPanel);

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(dropDown, BorderLayout.EAST);

        facultyBooks.setLayout(new BorderLayout());
        facultyBooks.add(headerPanel, BorderLayout.NORTH);
        facultyBooks.add(mainPanel, BorderLayout.CENTER);
        facultyBooks.setLocationRelativeTo(null);
        facultyBooks.setVisible(true);
    }

    // Button renderer
    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            setOpaque(true);
            deleteButton = new JButton(new ImageIcon(new ImageIcon("assets/delete_button.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            deleteButton.setBorderPainted(false);
            deleteButton.setContentAreaFilled(false);
            deleteButton.setFocusPainted(false);
            deleteButton.setOpaque(false);
            add(deleteButton);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    // Button editor
    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final JPanel panel;
        private final JTable table;
        private final DefaultTableModel model;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, JTable table) {
            super(checkBox);
            this.table = table;
            this.model = model;

            button = new JButton(new ImageIcon(new ImageIcon("assets/delete_button.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            button.setOpaque(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);

            // Wrap in panel like in ButtonRenderer
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panel.setOpaque(true);
            panel.add(button);

            button.addActionListener(e -> deleteRow(table.getSelectedRow()));
        }

        private void deleteRow(int row) {
            if (row >= 0) {
                String isbn = table.getValueAt(row, 3).toString(); // adjust column index as needed
                int totalCopies = Integer.parseInt(table.getValueAt(row, 4).toString()); // adjust column for total_copies

                Color originalOptionPaneBg = (Color) UIManager.get("OptionPane.background");
                Color originalPanelBg = (Color) UIManager.get("Panel.background");

                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);

                // Slider for selecting number of copies to delete
                JSlider slider = new JSlider(1, totalCopies, 1);
                slider.setMajorTickSpacing(1);
                slider.setPaintTicks(true);
                slider.setPaintLabels(true);
                slider.setBackground(Color.WHITE);
                slider.setForeground(Color.decode("#800201"));

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.WHITE); // Set the panel background to white

                JLabel label = new JLabel("Select number of copies to delete:");
                label.setForeground(Color.decode("#800201")); // Change label text color to red
                panel.add(label, BorderLayout.NORTH);
                panel.add(slider, BorderLayout.CENTER);

                int option = JOptionPane.showConfirmDialog(null, panel, "Delete Copies", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                UIManager.put("OptionPane.background", originalOptionPaneBg);
                UIManager.put("Panel.background", originalPanelBg);

                if (option == JOptionPane.OK_OPTION) {
                    int toDelete = slider.getValue();

                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete " + toDelete + " copy(ies)?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection con = DBConnection.connect()) {
                            if (toDelete < totalCopies) {
                                // Just update the number of copies
                                try (PreparedStatement stmt = con.prepareStatement("UPDATE book SET total_copies = total_copies - ? WHERE isbn_number = ?")) {
                                    stmt.setInt(1, toDelete);
                                    stmt.setString(2, isbn);
                                    int affected = stmt.executeUpdate();
                                    if (affected > 0) {
                                        table.setValueAt(totalCopies - toDelete, row, 4); // update UI table value
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Failed to update copies.");
                                    }
                                }
                            } else {
                                // Delete entire row
                                try (PreparedStatement stmt = con.prepareStatement("DELETE FROM book WHERE isbn_number = ?")) {
                                    stmt.setString(1, isbn);
                                    int affected = stmt.executeUpdate();
                                    if (affected > 0) {
                                        model.removeRow(row);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Failed to delete book.");
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error during deletion.");
                        }
                    }
                }
            }
        }



        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

}
