import javax.management.Notification;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;

public class BookInfo {
    public static void bookInfo(JFrame faculty) {
        // Main Frame Creation
        JFrame bookInfo = new JFrame();
        bookInfo.getContentPane().setBackground(Color.decode("#f4f6f9"));
        bookInfo.setTitle("PUP Library System/student/Dashboard/");
        bookInfo.setSize(600, 600);
        bookInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bookInfo.setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setSize(new Dimension(bookInfo.getWidth(), 70));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setOpaque(true);

        // Logo
        ImageIcon logo = new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setBorder((BorderFactory.createEmptyBorder(8, 8, 8, 0)));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel title = new JLabel("PUP LIBRARY");
        title.setFont(new Font("Roboto", Font.PLAIN, 23));
        title.setForeground(Color.decode("#800201"));
        title.setBorder((BorderFactory.createEmptyBorder(20, 4, 20, 10)));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBounds(9, 70, 565, 295);
        contentPanel.setBackground(Color.decode("#ebebeb"));
        contentPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        contentPanel.setLayout(null);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        formPanel.setBounds(20, 20, 525, 200);
        formPanel.setBackground(Color.decode("#ebebeb"));

        // Labels and Fields
        JLabel titleLabel = new JLabel("Book Title:");
        JTextField titleField = new JTextField();

        JLabel yearLabel = new JLabel("Year:");
        JTextField yearField = new JTextField();

        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();

        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField();

        JLabel bookCopies = new JLabel("Number of Copies:");
        JTextField copies = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        String[] categoryOptions = {"Computer Science", "Information Systems", "Electronics", "Mathematics"};
        JComboBox<String> categoryBox = new JComboBox<>(categoryOptions);

        formPanel.add(titleLabel); formPanel.add(titleField);
        formPanel.add(yearLabel); formPanel.add(yearField);
        formPanel.add(authorLabel); formPanel.add(authorField);
        formPanel.add(isbnLabel); formPanel.add(isbnField);
        formPanel.add(bookCopies); formPanel.add(copies);
        formPanel.add(categoryLabel); formPanel.add(categoryBox);

        // Submit Button
        JButton submitBtn = new JButton("Add Book");
        submitBtn.setBackground(Color.decode("#800201"));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Return Button
        JButton returnBtn = new JButton("Return");
        returnBtn.setBackground(Color.GRAY);
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFocusPainted(false);
        returnBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Centering Buttons Horizontally
        int panelWidth = contentPanel.getWidth(); // 565
        int buttonWidth = 120;
        int buttonHeight = 30;
        int spacing = 20;

        int totalWidth = (2 * buttonWidth) + spacing;
        int startX = (panelWidth - totalWidth) / 2;
        int buttonY = 240;

        returnBtn.setBounds(startX, buttonY, buttonWidth, buttonHeight);
        submitBtn.setBounds(startX + buttonWidth + spacing, buttonY, buttonWidth, buttonHeight);

        // Return logic to go back to FacultyBooks
        returnBtn.addActionListener(ev -> {
            bookInfo.setVisible(false);
            FacultyBooks.facBooks(bookInfo); // Open FacultyBooks window
        });

        // Submit Logic (with total copy aggregation logic)
        submitBtn.addActionListener(e -> {
            String bookTitle = titleField.getText();
            String year = yearField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            String copy = copies.getText();
            String category = (String) categoryBox.getSelectedItem();

            try {
                int addedCopies = Integer.parseInt(copy);

                try (Connection con = DBConnection.connect()) {
                    // Insert the new book
                    String insertSql = "INSERT INTO book (book_title, publication_year, book_author, isbn_number, total_copies, book_category) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setString(1, bookTitle);
                    insertStmt.setString(2, year);
                    insertStmt.setString(3, author);
                    insertStmt.setString(4, isbn);
                    insertStmt.setInt(5, addedCopies);
                    insertStmt.setString(6, category);

                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                        int insertedBookId = 0;
                        if (generatedKeys.next()) {
                            insertedBookId = generatedKeys.getInt(1);
                        }
                        generatedKeys.close();
                        insertStmt.close();

                        String insertNotif = "INSERT INTO notification (book_id, notification_date) VALUES (?, NOW())";
                        PreparedStatement notifStmt = con.prepareStatement(insertNotif);
                        notifStmt.setInt(1, insertedBookId);
                        notifStmt.executeUpdate();
                        notifStmt.close();

                        // Aggregate copies
                        String sumSql = "SELECT SUM(total_copies) FROM book WHERE book_title = ?";
                        PreparedStatement sumStmt = con.prepareStatement(sumSql);
                        sumStmt.setString(1, bookTitle);
                        ResultSet rs = sumStmt.executeQuery();

                        int newTotal = 0;
                        if (rs.next()) {
                            newTotal = rs.getInt(1);
                        }
                        rs.close();
                        sumStmt.close();

                        // Update total
                        String updateSql = "UPDATE book SET total_copies = ? WHERE book_title = ?";
                        PreparedStatement updateStmt = con.prepareStatement(updateSql);
                        updateStmt.setInt(1, newTotal);
                        updateStmt.setString(2, bookTitle);
                        updateStmt.executeUpdate();
                        updateStmt.close();

                        JOptionPane.showMessageDialog(bookInfo, "Book added successfully!");
                        titleField.setText("");
                        yearField.setText("");
                        authorField.setText("");
                        isbnField.setText("");
                        copies.setText("");
                        categoryBox.setSelectedIndex(0);
                        
                    }

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(bookInfo, "Database error: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(bookInfo, "Invalid number of copies: " + ex.getMessage());
            }
        });

        contentPanel.add(formPanel);
        contentPanel.add(submitBtn);
        contentPanel.add(returnBtn);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(bookInfo.getWidth(), 500));
        mainPanel.setBackground(Color.decode("#f4f6f9"));
        mainPanel.setLayout(null);
        mainPanel.setOpaque(true);

        mainPanel.add(contentPanel);

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);

        bookInfo.setLayout(new BorderLayout());
        bookInfo.add(headerPanel, BorderLayout.NORTH);
        bookInfo.add(mainPanel, BorderLayout.CENTER);
        bookInfo.setLocationRelativeTo(null);
        bookInfo.setVisible(true);
    }
}


