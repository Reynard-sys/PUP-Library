import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class notification {
    public static void notif() {
        JFrame notifFrame = new JFrame("Notification");
        notifFrame.setSize(500, 500);
        notifFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        notifFrame.getContentPane().setBackground(Color.decode("#FFFFFF"));
        notifFrame.setLayout(new BorderLayout());
        notifFrame.setResizable(false);
        notifFrame.setIconImage(new ImageIcon("assets/pup.png").getImage());
        notifFrame.setTitle("PUP Library System/notification/");

        // Fixed screen location
        Point fixedLocation = new Point(520, 187);
        notifFrame.setLocation(fixedLocation);
        notifFrame.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                notifFrame.setLocation(fixedLocation);
            }
        });

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(notifFrame.getWidth(), 70));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        headerPanel.setLayout(new BorderLayout());

        ImageIcon logo = new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));

        JLabel title = new JLabel("PUP LIBRARY");
        title.setFont(new Font("Roboto", Font.PLAIN, 23));
        title.setForeground(Color.decode("#800201"));
        title.setBorder(BorderFactory.createEmptyBorder(20, 4, 20, 10));
        title.setHorizontalAlignment(SwingConstants.LEFT);

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);

        // List Panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(null);
        listPanel.setBackground(Color.decode("#f4f6f9"));

        int yOffset = 10;
        int rowHeight = 90;
        int rowWidth = 450;

        try (Connection con = DBConnection.connect()) {
            String sql = """
                SELECT book.book_title, book.book_author, book.book_category, notification.notification_date, notification.notification_type
                FROM notification
                LEFT JOIN book ON notification.book_id = book.book_id
                ORDER BY notification.notification_date DESC
            """;

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String bookTitle = rs.getString("book_title");
                String bookAuthor = rs.getString("book_author");
                String bookCategory = rs.getString("book_category");
                Timestamp date = rs.getTimestamp("notification_date");
                String message = rs.getString("notification_type");

                if (bookTitle == null) {
                bookTitle = "[Deleted Book]";
                bookAuthor = "-";
                bookCategory = "-";
                }

                String notifText = "<html><b>" + bookTitle + "</b><br/>"
                + "Author: " + bookAuthor + "<br/>"
                + "Category: " + bookCategory + "<br/>"
                + "<i>" + message + " on " + date.toString() + "</i></html>";

                JPanel notifRow = new JPanel();
                notifRow.setLayout(null);
                notifRow.setBounds(10, yOffset, rowWidth, rowHeight);
                notifRow.setBackground(Color.decode("#FFFFFF"));
                notifRow.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JLabel notifLabel = new JLabel(notifText);
                notifLabel.setBounds(10, 5, rowWidth - 20, rowHeight - 10);
                notifLabel.setFont(new Font("Roboto", Font.PLAIN, 13));

                notifRow.add(notifLabel);
                listPanel.add(notifRow);

                yOffset += rowHeight + 10;
            }

            listPanel.setPreferredSize(new Dimension(notifFrame.getWidth(), yOffset));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(notifFrame, "Database error: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(listPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 🔴 Clear Button
        JButton clearButton = new JButton("Clear Notifications");
        clearButton.setBackground(Color.decode("#800201"));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Roboto", Font.PLAIN, 13));
        clearButton.setPreferredSize(new Dimension(200, 30));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(notifFrame, "Are you sure you want to clear all notifications?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection con = DBConnection.connect()) {
                    String deleteSql = "DELETE FROM notification";
                    PreparedStatement deleteStmt = con.prepareStatement(deleteSql);
                    deleteStmt.executeUpdate();
                    deleteStmt.close();
                    JOptionPane.showMessageDialog(notifFrame, "All notifications cleared.");
                    notifFrame.dispose(); // close current window
                    notif(); // reopen with fresh state
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(notifFrame, "Failed to clear notifications: " + ex.getMessage());
                }
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.decode("#f4f6f9"));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        bottomPanel.add(clearButton);
        
        notifFrame.add(headerPanel, BorderLayout.NORTH);
        notifFrame.add(scrollPane, BorderLayout.CENTER);
        notifFrame.add(bottomPanel, BorderLayout.SOUTH);
        notifFrame.setVisible(true);
    }
}
