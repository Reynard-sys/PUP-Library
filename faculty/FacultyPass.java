import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FacultyPass {
    static String student_number;
    static String student_password;
    static String purpose;
    public static void facPass(JFrame faculty)
    {
        //Main Frame Creation
        JFrame facultyPass = new JFrame();
        facultyPass.getContentPane().setBackground(Color.decode("#f4f6f9"));
        facultyPass.setTitle("PUP Library System/student/Pass/");
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

        notif.addActionListener(e -> {
            notification.notif();
            facultyPass.setEnabled(true); // Disable the main library window
        });

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
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("<html><span style='color:black;'>Welcome to </span><span style='color:#800201;'>PUP Library</span></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel subText = new JLabel("Log in to record your visit!");
        subText.setFont(new Font("Roboto", Font.PLAIN, 16));
        subText.setForeground(Color.DARK_GRAY);
        subText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputStudentPanel = new JPanel(new BorderLayout());
        inputStudentPanel.setMaximumSize(new Dimension(500, 40)); 
        inputStudentPanel.setBackground(Color.WHITE);
        inputStudentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Text field
        JTextField studentID = new JTextField("Student Number");
        studentID.setForeground(Color.GRAY); 
        studentID.setFont(new Font("Roboto", Font.PLAIN, 15));
        studentID.setBorder(null); 
        inputStudentPanel.add(studentID, BorderLayout.CENTER);

        // Placeholder behavior
        studentID.addFocusListener(new FocusAdapter() { 
            @Override
            public void focusGained(FocusEvent e) {
                if (studentID.getText().equals("Student Number")) {
                    studentID.setText("");
                    studentID.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (studentID.getText().isEmpty()) {
                    studentID.setText("Student Number");
                    studentID.setForeground(Color.GRAY);
                }
            }
        });
        inputStudentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputStudentPassword = new JPanel(new BorderLayout());
        inputStudentPassword.setMaximumSize(new Dimension(500, 40)); 
        inputStudentPassword.setBackground(Color.WHITE);
        inputStudentPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPasswordField studentPassword = new JPasswordField();
        studentPassword.setForeground(Color.GRAY); 
        studentPassword.setFont(new Font("Roboto", Font.PLAIN, 15));
        studentPassword.setBorder(null); 
        inputStudentPassword.add(studentPassword, BorderLayout.CENTER);

        // Text field
        String placeholder = "Password";
        studentPassword.setEchoChar((char) 0);  // Show text initially (placeholder)
        studentPassword.setForeground(Color.GRAY);
        studentPassword.setText(placeholder);

        // Placeholder behavior
        studentPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (String.valueOf(studentPassword.getPassword()).equals(placeholder))
                {
                    studentPassword.setText("");
                    studentPassword.setEchoChar('•'); 
                    studentPassword.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e)
            {
                if (String.valueOf(studentPassword.getPassword()).isEmpty())
                {
                    studentPassword.setEchoChar((char) 0);
                    studentPassword.setForeground(Color.GRAY);
                    studentPassword.setText(placeholder);
                }
            }
        });
        inputStudentPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputDescriptionPanel = new JPanel(new BorderLayout());
        inputDescriptionPanel.setMaximumSize(new Dimension(500, 40)); // To keep consistent with the text field
        inputDescriptionPanel.setBackground(Color.WHITE);
        inputDescriptionPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Text field
        JTextField descriptionID = new JTextField("Purpose of Visit");
        descriptionID.setForeground(Color.GRAY); 
        descriptionID.setFont(new Font("Roboto", Font.PLAIN, 15));
        descriptionID.setBorder(null); // remove default border to blend in
        inputDescriptionPanel.add(descriptionID, BorderLayout.CENTER);

        // Placeholder behavior
        descriptionID.addFocusListener(new FocusAdapter() { 
            @Override
            public void focusGained(FocusEvent e) {
                if (descriptionID.getText().equals("Purpose of Visit")) {
                    descriptionID.setText("");
                    descriptionID.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (descriptionID.getText().isEmpty()) {
                    descriptionID.setText("Purpose of Visit");
                    descriptionID.setForeground(Color.GRAY);
                }
            }
        });
        inputDescriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        Font buttonFont = new Font("Poppins", Font.PLAIN, 18);
        Dimension buttonSize = new Dimension(500, 50);

        JButton study = createStyledButton("Enter Library", buttonFont, buttonSize);
        study.setBackground(Color.decode("#d63f4a"));
        study.setAlignmentX(Component.CENTER_ALIGNMENT);

        study.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                study.setBackground(Color.decode("#B71C1C"));
            }
            public void mouseExited(MouseEvent e) {
                study.setBackground(Color.decode("#d63f4a"));
            }
        });

        study.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                student_number = studentID.getText();
                student_password = String.valueOf(studentPassword.getPassword());
                purpose = descriptionID.getText();

                boolean isPasswordPlaceholder = student_password.equals("Password");
                boolean isIDPlaceholder = student_number.equals("Student Number");
                boolean isPurposePlaceholder = purpose.equals("Purpose of Visit");

                boolean idEmpty = student_number.isEmpty() || isIDPlaceholder;
                boolean passwordEmpty = student_password.isEmpty() || isPasswordPlaceholder;
                boolean purposeEmpty = purpose.isEmpty() || isPurposePlaceholder;

                if (idEmpty && passwordEmpty)
                {
                    JOptionPane.showMessageDialog
                    (
                        faculty,
                        "The Student Number field is required.\n\nThe Password field is required.",
                        "Alert!", JOptionPane.ERROR_MESSAGE
                    );
                }
                else if (idEmpty)
                {
                    JOptionPane.showMessageDialog
                    (
                        faculty,
                        "The Student Number field is required.",
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
                else if (purposeEmpty)
                {
                    JOptionPane.showMessageDialog(
                        faculty, 
                        "The Purpose of Visit field is required.",
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
                                "SELECT * FROM student_login WHERE student_no = ? AND password = ?"
                            );
                            stmt.setString(1, student_number);
                            stmt.setString(2, student_password);

                            ResultSet rs = stmt.executeQuery();

                            if (rs.next()) {
                                int studentLoginId = rs.getInt("student_login_id");
                                PreparedStatement getStudentId = con.prepareStatement(
                                    "SELECT student_id, student_name FROM student WHERE student_login_id = ?"
                                );
                                
                                getStudentId.setInt(1, studentLoginId);
                                ResultSet studentRs = getStudentId.executeQuery();

                                if (studentRs.next()) {
                                    int studentId = studentRs.getInt("student_id");
                                    String name = studentRs.getString("student_name");
                                    // Step 2: Insert time-in into library_physical table
                                    PreparedStatement insertLog = con.prepareStatement(
                                        "INSERT INTO library_physical (student_id, entry_date, entry_time, purpose) VALUES (?, CURDATE(), CURTIME(), ?)"
                                    );
                                    insertLog.setInt(1, studentId);
                                    insertLog.setString(2, purpose); // You can change the purpose if needed
                                    insertLog.executeUpdate();
                                    
                                    JOptionPane.showMessageDialog(
                                        faculty,
                                        "<html><div style='text-align: center;'>Welcome, <b>" + name + "</b>!<br>" +
                                        "You're now logged in.<br>Please follow our library's rules.</div></html>",
                                  "Library Visitor's Log",
                                        JOptionPane.INFORMATION_MESSAGE
                                    );
                                    resetPlaceholder(studentID, "Student Number");
                                    resetPasswordField(studentPassword, "Password");
                                    resetPlaceholder(descriptionID, "Purpose of Visit");

                                }
                            }
                            else {
                                JOptionPane.showMessageDialog
                                (
                                    faculty,
                                    "Incorrect login credentials",
                                    "Alert!", JOptionPane.ERROR_MESSAGE
                                );
                                resetPlaceholder(studentID, "Student Number");
                                resetPasswordField(studentPassword, "Password");
                                resetPlaceholder(descriptionID, "Purpose of Visit");
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
  
        //Container for everything after the header
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(facultyPass.getWidth(), 500));
        mainPanel.setBackground(Color.decode("#f4f6f9"));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);
        mainPanel.setLayout(null);
        
        
        contentPanel.add(welcomeLabel);
        contentPanel.add(subText);
        contentPanel.add(Box.createVerticalStrut(20)); // spacing
        contentPanel.add(inputStudentPanel);
        contentPanel.add(Box.createVerticalStrut(20)); // spacing
        contentPanel.add(inputStudentPassword);
        contentPanel.add(Box.createVerticalStrut(20)); // spacing
        contentPanel.add(inputDescriptionPanel);
        contentPanel.add(Box.createVerticalStrut(20)); // spacing
        contentPanel.add(study);


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

    public static JButton createStyledButton(String text, Font font, Dimension size)
    {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
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
