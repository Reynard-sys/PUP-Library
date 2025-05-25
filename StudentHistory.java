import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentHistory {
    public static void studHistory(JFrame studentDashboard)
    {
        JFrame studentHistory = new JFrame();
        studentHistory.getContentPane().setBackground(Color.decode("#f4f6f9"));
        studentHistory.setTitle("PUP Library System/student/History/");
        studentHistory.setSize(600,600);
        studentHistory.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentHistory.setResizable(false);

        JPanel headerPanel = new JPanel();
        headerPanel.setSize(new Dimension(studentHistory.getWidth(), 70));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setOpaque(true);

        ImageIcon logo = new ImageIcon(new ImageIcon("pup.png").getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setBorder((BorderFactory.createEmptyBorder(8, 8, 8, 0)));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("PUP LIBRARY");
        title.setFont(new Font("Roboto", Font.PLAIN, 23));
        title.setForeground(Color.decode("#800201"));
        title.setBorder((BorderFactory.createEmptyBorder(20, 4, 20, 10)));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton dropDown = new JButton();
        ImageIcon icon = new ImageIcon(new ImageIcon("dropdownBlack.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        dropDown.setIcon(icon);
        dropDown.setFocusPainted(false);
        dropDown.setBorderPainted(false);
        dropDown.setContentAreaFilled(false);
        dropDown.setBorder((BorderFactory.createEmptyBorder(20, 10, 20,35)));
        dropDown.setHorizontalAlignment(SwingConstants.RIGHT);
        dropDown.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPopupMenu menu = new JPopupMenu();
        menu.setPreferredSize(new Dimension(170, 60));
        menu.setBackground(Color.WHITE);
        menu.setBackground(Color.WHITE);
        UIManager.put("PopupMenu.background", Color.WHITE);
        UIManager.put("MenuItem.background", Color.WHITE);
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.decode("#800201"))); 

        JMenuItem notif = new JMenuItem("Notification");
        ImageIcon notif_icon = new ImageIcon(new ImageIcon("notification.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        notif.setIcon(notif_icon);
        notif.setPreferredSize(new Dimension(170, 30));

        JMenuItem sign_out = new JMenuItem("Sign out");
        ImageIcon signout_icon = new ImageIcon(new ImageIcon("logout.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
        sign_out.setIcon(signout_icon);
        sign_out.setPreferredSize(new Dimension(170, 30));

        sign_out.addActionListener(e -> 
        {
            studentHistory.dispose(); 
            SwingUtilities.invokeLater(() -> Main.main(new String[]{}));
        });

        JPanel separatorPanel = new JPanel();
        separatorPanel.setPreferredSize(new Dimension(170, 1));
        separatorPanel.setBackground(Color.BLACK);

        menu.add(notif);
        menu.add(separatorPanel);
        menu.add(sign_out);

        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.show(dropDown, -145, dropDown.getHeight()-20);
            }
        });

        JLayeredPane layeredNav = new JLayeredPane();
        layeredNav.setPreferredSize(new Dimension(500, 80));
        layeredNav.setLayout(null); 

        JPanel roundedNav = new JPanel()
        {
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#800201"));
                g2.fillRoundRect(0, 0, 485, 53, 50, 50);
            }
        };
        roundedNav.setBounds(50, 20, 485, 53);
        roundedNav.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(60, 25, 460, 40);


        buttonPanel.add(wrapButton("Dashboard", studentHistory, StudentDashboard.class));
        buttonPanel.add(createDivider());
        buttonPanel.add(wrapButton("My Books", studentHistory, StudentBooks.class));
        buttonPanel.add(createDivider());
        buttonPanel.add(wrapButton("History", studentHistory, StudentHistory.class));

        layeredNav.add(roundedNav, JLayeredPane.DEFAULT_LAYER);
        layeredNav.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(studentHistory.getWidth(), 500));
        mainPanel.setBackground(Color.decode("#f4f6f9"));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);

        mainPanel.add(layeredNav, BorderLayout.CENTER);

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(dropDown, BorderLayout.EAST);

        studentHistory.setLayout(new BorderLayout());
        studentHistory.add(headerPanel, BorderLayout.NORTH);
        studentHistory.add(mainPanel, BorderLayout.CENTER);
        studentHistory.setLocationRelativeTo(null);
        studentHistory.setVisible(true);

    }

    private static JPanel wrapButton(String label, JFrame studentHistory, Class<?> frameClass) 
    {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JButton btn = createMenuButton(label, studentHistory, frameClass);
        wrapper.add(btn, BorderLayout.CENTER);

        return wrapper;
    }

    private static JButton createMenuButton(String label, JFrame studentHistory, Class<?> frameClass)
    {
        JButton choices = new JButton(label);
        choices.setForeground(Color.WHITE);
        choices.setFont(new Font("SansSerif", Font.BOLD, 15));
        choices.setFocusPainted(false);
        choices.setContentAreaFilled(false);
        choices.setOpaque(false);
        choices.setBorder(BorderFactory.createEmptyBorder());
        choices.setBorderPainted(false);

        if (label == "Dashboard")
        {
            choices.setHorizontalAlignment(SwingConstants.RIGHT); 
        }
        else if (label == "My Books")
        {
            choices.setHorizontalAlignment(SwingConstants.CENTER); 
        }
        else if (label == "History")
        {
            choices.setHorizontalAlignment(SwingConstants.LEFT); 
            choices.setForeground(Color.decode("#fede07"));

        }
        else if (label != "History")
        {
            choices.addMouseListener(new MouseAdapter() 
            {
                public void mouseEntered(MouseEvent e) 
                {
                    choices.setForeground(Color.YELLOW);
                }

                public void mouseExited(MouseEvent e) 
                {
                    choices.setForeground(Color.WHITE);
                }
            });
        }
        
        choices.setCursor(new Cursor(Cursor.HAND_CURSOR));

        choices.addActionListener(e -> {
            try 
            {
                if (frameClass == StudentDashboard.class)
                {
                    studentHistory.setVisible(false); 
                    StudentDashboard.studDashboard(studentHistory);
                }
                else if (frameClass == StudentBooks.class) 
                {
                    studentHistory.setVisible(false); 
                    StudentBooks.studBook(studentHistory);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return choices;
    }

    private static JPanel createDivider()
    {
        JPanel dividerPanel = new JPanel() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                g.setColor(Color.BLACK); // Line color
                g.fillRect(getWidth()/2 - 1, 0, 2, getHeight()); // Draws a 2px wide vertical line
            }
        };
        dividerPanel.setPreferredSize(new Dimension(4, 50)); // Adjust width & height
        dividerPanel.setOpaque(false);

        return dividerPanel;
    }
}
