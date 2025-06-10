import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class FacultyLogin {
    static String faculty_user;
    static String faculty_password;
    static int login_attempt = 0;
    public static void window(JFrame landing)
    {
        //Student Login Frame Creation
        JFrame faculty = new JFrame();
        BackgroundPanel background = new BackgroundPanel("assets/library.jpg", 0.3f);
        background.setBackground(Color.WHITE);
        faculty.setContentPane(background);
        faculty.setTitle("PUP Library System/faculty/");
        faculty.setSize(600,600);
        faculty.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        faculty.setResizable(false);

        
        //PUP Logo
        ImageIcon logo = new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Container for top elements
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(32, 10, 10, 10));

        ImageIcon backIcon = new ImageIcon(new ImageIcon("assets/bbutton.png").getImage().getScaledInstance(50, 55, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setToolTipText("Back");
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> {
            faculty.dispose();  // Correct usage
            SwingUtilities.invokeLater(() -> Main.main(new String[]{}));
        });

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, -26));
        leftHeader.setOpaque(false);
        leftHeader.add(backButton);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.setOpaque(false);


        //Title text
        JLabel title = new JLabel("PUP Library Faculty Module");
        title.setFont(new Font("Poppins", Font.BOLD, 35));
        title.setBorder((BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        title.setForeground(Color.decode("#700000"));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Instruction text
        JLabel instruct = new JLabel("Sign in to start your session");
        instruct.setFont(new Font("Roboto", Font.PLAIN, 20));
        instruct.setBorder((BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        instruct.setHorizontalAlignment(SwingConstants.CENTER);
        instruct.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Input box for User Name
        JTextField userName = new JTextField("User Name");
        userName.setForeground(Color.GRAY);
        userName.setFont(new Font("Roboto", Font.PLAIN, 15));
        userName.setPreferredSize(new Dimension(500, 40));
        userName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (userName.getText().equals("User Name"))
                {
                    userName.setText("");
                    userName.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e)
            {
                if (userName.getText().isEmpty())
                {
                    userName.setText("User Name");
                    userName.setForeground(Color.GRAY);
                }
            }
        });

        //Input box for Password
        JPasswordField password = new JPasswordField();
        password.setFont(new Font("Roboto", Font.PLAIN, 15));
        password.setPreferredSize(new Dimension(500, 40));

        String placeholder = "Password";
        password.setEchoChar((char) 0);  // Show text initially (placeholder)
        password.setForeground(Color.GRAY);
        password.setText(placeholder);

        password.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (String.valueOf(password.getPassword()).equals(placeholder))
                {
                    password.setText("");
                    password.setEchoChar('•'); 
                    password.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e)
            {
                if (String.valueOf(password.getPassword()).isEmpty())
                {
                    password.setEchoChar((char) 0);
                    password.setForeground(Color.GRAY);
                    password.setText(placeholder);
                }
            }
        });

        Font buttonFont = new Font("Poppins", Font.PLAIN, 18);
        Dimension buttonSize = new Dimension(500, 50);

        JButton sign_in = Main.createStyledButton("Sign in", buttonFont, buttonSize);
        sign_in.setBackground(Color.decode("#257cf6"));
        sign_in.setAlignmentX(Component.CENTER_ALIGNMENT);
        UIManager.put("OptionPane.background", Color.decode("#dc3546"));
        UIManager.put("Panel.background", Color.decode("#dc3546"));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        sign_in.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                sign_in.setBackground(Color.decode("#0D47A1"));
            }
            public void mouseExited(MouseEvent e) {
                sign_in.setBackground(Color.decode("#257cf6"));
            }
        });

        sign_in.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                faculty_user = userName.getText();
                faculty_password = String.valueOf(password.getPassword());

                boolean isPasswordPlaceholder = faculty_password.equals("Password");
                boolean isIDPlaceholder = faculty_user.equals("User Name");

                boolean idEmpty = faculty_user.isEmpty() || isIDPlaceholder;
                boolean passwordEmpty = faculty_password.isEmpty() || isPasswordPlaceholder;

                if (idEmpty && passwordEmpty)
                {
                    JOptionPane.showMessageDialog
                    (
                        faculty,
                        "The Username field is required.\n\nThe Password field is required.",
                        "Alert!", JOptionPane.ERROR_MESSAGE
                    );
                }
                else if (idEmpty)
                {
                    JOptionPane.showMessageDialog
                    (
                        faculty,
                        "The Username field is required.",
                        "Alert!", JOptionPane.ERROR_MESSAGE
                    );
                }
                else if (passwordEmpty)
                {
                    JOptionPane.showMessageDialog
                    (
                        faculty,
                        "The Password field is required.",
                        "Alert!", JOptionPane.ERROR_MESSAGE
                    );
                }
                else
                {
                    // All fields filled correctly; proceed
                    try{
                        Connection con = DBConnection.connect();
                        if(con != null){
                            PreparedStatement stmt = con.prepareStatement(
                                "SELECT * FROM faculty_login WHERE username=? and password=?"
                            );
                            stmt.setString(1, faculty_user);
                            stmt.setString(2, faculty_password);

                            ResultSet rs = stmt.executeQuery();

                            if (rs.next()) {
                                JOptionPane.showMessageDialog(faculty, "✅ Login successful!");
                                faculty.setVisible(false);
                                FacultyBooks.facBooks(faculty);
                            } else {
                                login_attempt ++;
                                int attemptsRemaining = 3 - login_attempt;
                                JOptionPane.showMessageDialog
                                (
                                    faculty,
                                    "Incorrect login credentials (Attempt/s remaining: " + attemptsRemaining + ")",
                                    "Alert!", JOptionPane.ERROR_MESSAGE
                                );
                                resetPlaceholder(userName, "User Name");
                                resetPasswordField(password, "Password");
                                if (login_attempt >= 3)
                                {
                                    JOptionPane.showMessageDialog
                                    (
                                        faculty, "Oops! Maximum log in attempts exceeded.\nClosing program.",
                                        "Alert!", JOptionPane.ERROR_MESSAGE
                                    );
                                    System.exit(0);
                                }
                            }
                            rs.close();
                            stmt.close();
                            con.close();
                        }else{
                            JOptionPane.showMessageDialog(faculty, "🚫 Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch(Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(faculty, "⚠️ An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        //Hyperlink creation
        JLabel terms = new JLabel("<html><a href=''>Terms of Use</a> and </html>");
        terms.setFont(new Font("Roboto", Font.PLAIN, 12));
        terms.setBackground(Color.decode("#0000EE"));
        terms.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel statement = new JLabel("<html><a href=''>Privacy Statement</a></html>");
        statement.setFont(new Font("Roboto", Font.PLAIN, 12));
        statement.setBackground(Color.decode("#0000EE"));
        statement.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Note creation
        JLabel note = new JLabel("By using this service, you understand and agree to the PUP Online Services\n");
        note.setFont(new Font("Roboto", Font.PLAIN, 12));
        note.setBorder((BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        note.setHorizontalAlignment(SwingConstants.CENTER);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Container to store the link horizontally
        JPanel links = new JPanel();
        links.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        links.setOpaque(false);
        links.add(terms);
        links.add(statement);

        //Container to store note and links vertically
        JPanel footnote = new JPanel();
        footnote.setLayout(new BoxLayout(footnote, BoxLayout.Y_AXIS));
        footnote.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        footnote.setOpaque(false);
        footnote.add(note);
        footnote.add(links);

        //Addting MouseListener to go over the links when clicked
        terms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.pup.edu.ph/terms/"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        statement.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.pup.edu.ph/privacy/"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Adding MouseListener to go to the landing page when clicked
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                faculty.dispose();
                Main.main(null);
            }
        });

        //Container to store elements vertically
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 110, 50));
        loginPanel.add(userName);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(password);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(sign_in);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(footnote);

        topPanel.add(headerPanel);
        topPanel.add(logoLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(title);
        topPanel.add(instruct);

        faculty.setLayout(new BorderLayout());
        faculty.add(topPanel, BorderLayout.NORTH);
        faculty.add(loginPanel, BorderLayout.SOUTH);
        faculty.setLocationRelativeTo(null);
        faculty.setVisible(true);
    }

    public static void resetPlaceholder(JTextField field, String placeholder) 
    {
        field.setText("");
        if (!field.hasFocus()) 
        {
            field.setText(placeholder);
            field.setForeground(Color.GRAY);
        }
    }

    private static void resetPasswordField(JPasswordField field, String placeholder) {
        field.setEchoChar((char) 0);
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
    }
}
