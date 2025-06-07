import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FacultyPass {
    public static void facPass(JFrame faculty)
    {
        //Main Frame Creation
        JFrame facultyPass = new JFrame();
        facultyPass.getContentPane().setBackground(Color.decode("#f4f6f9"));
        facultyPass.setTitle("PUP Library System/student/Dashboard/");
        facultyPass.setSize(600,600);
        facultyPass.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        facultyPass.setResizable(false);

        //Container for logo, title, and drop menu
        JPanel headerPanel = new JPanel();
        headerPanel.setSize(new Dimension(facultyPass.getWidth(), 70));
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

        //When sign out is clicked, it will relaunch from landing page
        sign_out.addActionListener(e -> 
        {
            facultyPass.dispose(); 
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
        JPanel facultyNav = new JPanel();
        facultyNav.setLayout(new GridLayout(1, 4));
        facultyNav.setBounds(0, 0, facultyPass.getWidth(), 53);
        facultyNav.setBackground(Color.BLACK);
        facultyNav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        facultyNav.setOpaque(false);

        String[] buttonNames = {"Books", "Library Pass", "Log"};
        
        for (int i = 0; i < 3; i++)
        {
            JPanel subPanel = new JPanel(new BorderLayout());
            subPanel.setBackground(Color.decode("#800201"));

            if (i < 3)
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

            if (button.getText().equals("Library Pass")) 
            {
                button.setForeground(Color.YELLOW);
            }
            facultyNav.add(subPanel);

            button.addActionListener(e -> 
            {
                try 
                {
                    if (button.getText().equals("Books"))
                    {
                        facultyPass.setVisible(false); 
                        FacultyBooks.facBooks(facultyPass);
                    }
                    else if (button.getText().equals("Log"))
                    {
                        facultyPass.setVisible(false); 
                        FacultyLog.facLog(facultyPass);
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
  
        //Container for everything after the header
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(facultyPass.getWidth(), 500));
        mainPanel.setBackground(Color.decode("#f4f6f9"));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);
        mainPanel.setLayout(null);

        mainPanel.add(facultyNav);
        mainPanel.add(contentPanel);    

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(dropDown, BorderLayout.EAST);

        facultyPass.setLayout(new BorderLayout());
        facultyPass.add(headerPanel, BorderLayout.NORTH);
        facultyPass.add(mainPanel, BorderLayout.CENTER);
        facultyPass.setLocationRelativeTo(null);
        facultyPass.setVisible(true);
    }
}
