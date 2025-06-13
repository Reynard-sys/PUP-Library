import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.table.*;


public class StudentHistory {
    public static void studHistory(JFrame studentLibrary)
    {
        JFrame studentHistory = new JFrame();
        studentHistory.getContentPane().setBackground(Color.decode("#f4f6f9"));
        studentHistory.setTitle("PUP Library System/student/History/");
        studentHistory.setSize(600,600);
        studentHistory.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentHistory.setResizable(false);

        //Container for logo, title, and drop menu
        JPanel headerPanel = new JPanel();
        headerPanel.setSize(new Dimension(studentHistory.getWidth(), 70));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setOpaque(true);

        //PUP Icon
        ImageIcon logo = new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setBorder((BorderFactory.createEmptyBorder(8, 8, 8, 0)));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Title
        JLabel title = new JLabel("PUP LIBRARY");
        title.setFont(new Font("Roboto", Font.PLAIN, 23));
        title.setForeground(Color.decode("#800201"));
        title.setBorder((BorderFactory.createEmptyBorder(20, 4, 20, 10)));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Creation of drop down menu
        JButton dropDown = new JButton();
        ImageIcon icon = new ImageIcon(new ImageIcon("assets/dropdownBlack.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        dropDown.setIcon(icon);
        dropDown.setFocusPainted(false);
        dropDown.setBorderPainted(false);
        dropDown.setContentAreaFilled(false);
        dropDown.setBorder((BorderFactory.createEmptyBorder(20, 10, 20,35)));
        dropDown.setHorizontalAlignment(SwingConstants.RIGHT);
        dropDown.setAlignmentX(Component.RIGHT_ALIGNMENT);

        //Creation of the pop up
        JPopupMenu menu = new JPopupMenu();
        menu.setPreferredSize(new Dimension(170, 60));
        menu.setBackground(Color.WHITE);
        menu.setBackground(Color.WHITE);
        UIManager.put("PopupMenu.background", Color.WHITE);
        UIManager.put("MenuItem.background", Color.WHITE);
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.decode("#800201"))); 

        //Notification Item
        JMenuItem notif = new JMenuItem("Notification");
        ImageIcon notif_icon = new ImageIcon(new ImageIcon("assets/notification.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        notif.setIcon(notif_icon);
        notif.setPreferredSize(new Dimension(170, 30));

        //Sign out Item
        JMenuItem sign_out = new JMenuItem("Sign out");
        ImageIcon signout_icon = new ImageIcon(new ImageIcon("assets/logout.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
        sign_out.setIcon(signout_icon);
        sign_out.setPreferredSize(new Dimension(170, 30));

        notif.addActionListener(e -> {
            notification.notif();
            studentHistory.setEnabled(true); // Disable the main library window
        });

        //When sign out is clicked, it will relaunch from landing page
        sign_out.addActionListener(e -> 
        {
            studentHistory.dispose(); 
            SwingUtilities.invokeLater(() -> Main.main(new String[]{}));
        });

        //Black separator line between notif and sign out
        JPanel separatorPanel = new JPanel();
        separatorPanel.setPreferredSize(new Dimension(170, 1));
        separatorPanel.setBackground(Color.BLACK);

        //Adding the elements to the pop up
        menu.add(notif);
        menu.add(separatorPanel);
        menu.add(sign_out);

        //When drop down is clicked, pop up menu will display
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.show(dropDown, -145, dropDown.getHeight()-20);
            }
        });

        //Navbar
        JPanel StudentNav = new JPanel();
        StudentNav.setLayout(new GridLayout(1, 4));
        StudentNav.setBounds(0, 0, studentHistory.getWidth(), 53);
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

            if (button.getText().equals("Library Visit History")) 
            {
                button.setForeground(Color.YELLOW);
            }
            StudentNav.add(subPanel);

            button.addActionListener(e -> 
            {
                try 
                {
                    if (button.getText().equals("Library Books")) 
                    {
                        studentHistory.setVisible(false); 
                        StudentLibrary.studLibrary(studentHistory);
                    }
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                }
            });
        }

        //Container for the main contents
        JPanel contentPanel = new JPanel();
        contentPanel.setBounds(9, 70, 565, 395); // Adjusting position manually
        contentPanel.setBackground(Color.decode("#ebebeb"));
        contentPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        String[] columns = {"Entry Date", "Entry Time", "Purpose"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            Connection con = DBConnection.connect();

            String sql = """
                SELECT lp.entry_date, lp.entry_time, lp.purpose
                FROM library_physical lp
                WHERE lp.student_id = ?
                ORDER BY lp.entry_date DESC, lp.entry_time DESC
                """;

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, StudentSession.studentId); // Use the logged-in student's ID
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Date entryDate = rs.getDate("entry_date");
                Time entryTime = rs.getTime("entry_time");
                String purpose = rs.getString("purpose");

                model.addRow(new Object[]{entryDate, entryTime, purpose});
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //table
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(565, 385));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // style
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setReorderingAllowed(false);

        //center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
  
        //Container for everything after the header
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(studentHistory.getWidth(), 500));
        mainPanel.setBackground(Color.decode("#f4f6f9"));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);
        mainPanel.setLayout(null);

        mainPanel.add(StudentNav);
        mainPanel.add(contentPanel);    

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(dropDown, BorderLayout.EAST);

        studentHistory.setLayout(new BorderLayout());
        studentHistory.add(headerPanel, BorderLayout.NORTH);
        studentHistory.add(mainPanel, BorderLayout.CENTER);
        studentHistory.setLocationRelativeTo(null);
        studentHistory.setVisible(true);
    }
}