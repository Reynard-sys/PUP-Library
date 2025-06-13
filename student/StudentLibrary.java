import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.*;


public class StudentLibrary {
    public static void studLibrary(JFrame student)
    {
        JFrame studentLibrary = new JFrame();
        studentLibrary.getContentPane().setBackground(Color.decode("#f4f6f9"));
        studentLibrary.setTitle("PUP Library System/student/Library/");
        studentLibrary.setSize(600,600);
        studentLibrary.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentLibrary.setResizable(false);

        JPanel headerPanel = new JPanel();
        headerPanel.setSize(new Dimension(studentLibrary.getWidth(), 70));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setOpaque(true);

        ImageIcon logo = new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setBorder((BorderFactory.createEmptyBorder(8, 8, 8, 0)));

        JLabel title = new JLabel("PUP LIBRARY");
        title.setFont(new Font("Roboto", Font.PLAIN, 23));
        title.setForeground(Color.decode("#800201"));
        title.setBorder((BorderFactory.createEmptyBorder(20, 4, 20, 10)));
        title.setHorizontalAlignment(SwingConstants.LEFT);

        JButton dropDown = new JButton();
        ImageIcon icon = new ImageIcon(new ImageIcon("assets/dropdownBlack.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        dropDown.setIcon(icon);
        dropDown.setFocusPainted(false);
        dropDown.setBorderPainted(false);
        dropDown.setContentAreaFilled(false);
        dropDown.setBorder((BorderFactory.createEmptyBorder(20, 10, 20, 35)));
        dropDown.setHorizontalAlignment(SwingConstants.RIGHT);

        JPopupMenu menu = new JPopupMenu();
        menu.setPreferredSize(new Dimension(170, 60));
        menu.setBackground(Color.WHITE);
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.decode("#800201")));

        JMenuItem notif = new JMenuItem("Notification");
        notif.setIcon(new ImageIcon(new ImageIcon("assets/notification.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        notif.setPreferredSize(new Dimension(170, 30));

        JMenuItem sign_out = new JMenuItem("Sign out");
        sign_out.setIcon(new ImageIcon(new ImageIcon("assets/logout.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        sign_out.setPreferredSize(new Dimension(170, 30));

        notif.addActionListener(e -> {
            notification.notif();
            studentLibrary.setEnabled(true); // Disable the main library window
        });

        sign_out.addActionListener(e -> {
            studentLibrary.dispose();
            SwingUtilities.invokeLater(() -> Main.main(new String[]{}));
        });

        JPanel separatorPanel = new JPanel();
        separatorPanel.setPreferredSize(new Dimension(170, 1));
        separatorPanel.setBackground(Color.BLACK);

        menu.add(notif);
        menu.add(separatorPanel);
        menu.add(sign_out);

        dropDown.addActionListener(e -> menu.show(dropDown, -145, dropDown.getHeight() - 20));
        //Navbar
        JPanel StudentNav = new JPanel();
        StudentNav.setLayout(new GridLayout(1, 4));
        StudentNav.setBounds(0, 0, studentLibrary.getWidth(), 53);
        StudentNav.setBackground(Color.BLACK);
        StudentNav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        StudentNav.setOpaque(false);

        String[] buttonNames = {"Library Books", "Library Visit History"};
        
        for (int i = 0; i < 2; i++)
        {
            JPanel subPanel = new JPanel(new BorderLayout());
            subPanel.setBackground(Color.decode("#800201"));

            if (i < 2)
            {
                subPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));
            }
            JButton button = new JButton(buttonNames[i]);
            button.setBackground(Color.decode("#800201"));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFocusable(false);
            button.setBorderPainted(false);
            button.setFocusable(false);
            subPanel.add(button, BorderLayout.CENTER);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (button.getText().equals("Library Books"))
            {
                button.setForeground(Color.YELLOW);
            }
            StudentNav.add(subPanel);

            button.addActionListener(e ->
            {
                try
                {
                    if (button.getText().equals("Library Visit History"))
                    {
                        studentLibrary.setVisible(false);
                        StudentHistory.studHistory(studentLibrary);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            });
        }

        String[] columns = {"Book Title", "Year", "Author", "ISBN", "Available", "Category"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            Connection con = DBConnection.connect();
            String sql = "SELECT * FROM book ORDER BY book_title ASC";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] row = {
                    rs.getString("book_title"),
                    String.valueOf(rs.getInt("publication_year")),
                    rs.getString("book_author"),
                    rs.getString("isbn_number"),
                    String.valueOf(rs.getInt("total_copies")),
                    rs.getString("book_category")
                };
                model.addRow(row);
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(0).setCellRenderer(new MultiLineCellRenderer()); // Book Title
        table.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer()); // Author

        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        // Optional: Set preferred column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

                //Container for the main contents
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode("#ebebeb"));
        contentPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBounds(9, 130, 565, 330);

        JPanel searchFilterPanel = new JPanel(new BorderLayout(10, 0));
        searchFilterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchFilterPanel.setBackground(Color.decode("#f4f6f9"));

        JTextField searchField = new JTextField() {
            private final String placeholder = "Search book title...";

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.setColor(Color.GRAY);
                    Insets insets = getInsets();
                    g2.drawString(placeholder, insets.left + 5, g.getFontMetrics().getMaxAscent() + insets.top + 2);
                    g2.dispose();
                }
            }
        };
        searchField.setPreferredSize(new Dimension(250, 50));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true), // true = rounded corners
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JComboBox<String> categoryFilter = new JComboBox<>(new String[]{"All", "Computer Science", "Information Systems", "Electronics", "Mathematics"});

        searchFilterPanel.add(searchField, BorderLayout.CENTER);
        searchFilterPanel.add(categoryFilter, BorderLayout.EAST);

        Runnable filterAndSort = () -> {
            String searchText = searchField.getText().trim().toLowerCase();
            String selectedCategory = ((String) categoryFilter.getSelectedItem()).toLowerCase();

            RowFilter<DefaultTableModel, Object> rf = new RowFilter<>() {
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String title = entry.getStringValue(0).toLowerCase(); // Book Title
                    String category = entry.getStringValue(5).toLowerCase(); // Category
                    boolean matchesSearch = title.contains(searchText);
                    boolean matchesCategory = selectedCategory.equals("all") || category.equals(selectedCategory);
                    return matchesSearch && matchesCategory;
                }
            };
            sorter.setRowFilter(rf);
        };

        javax.swing.event.DocumentListener docListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterAndSort.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterAndSort.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterAndSort.run(); }
        };
        searchField.getDocument().addDocumentListener(docListener);

        categoryFilter.addActionListener(e -> filterAndSort.run());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(565, 385));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(studentLibrary.getWidth(), 500));
        mainPanel.setBackground(Color.decode("#f4f6f9"));
        mainPanel.setLayout(null);

        searchFilterPanel.setBounds(120, 70, 350, 50);
        mainPanel.add(StudentNav);
        mainPanel.add(searchFilterPanel);
        mainPanel.add(contentPanel);

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(dropDown, BorderLayout.EAST);

        studentLibrary.setLayout(new BorderLayout());
        studentLibrary.add(headerPanel, BorderLayout.NORTH);
        studentLibrary.add(mainPanel, BorderLayout.CENTER);
        studentLibrary.setLocationRelativeTo(null);
        studentLibrary.setVisible(true);
    }
    

    static class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            setFont(table.getFont());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            int preferredHeight = getPreferredSize().height;
            if (table.getRowHeight(row) < preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }
            return this;
        }
    }
}

