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


public class StudentLogin {
    static String student_number;
    static String student_password;

    static int login_attempt = 0;
    public static void window(JFrame landing)
    {
        //Student Login Frame Creation
        JFrame student = new JFrame();
        BackgroundPanel background = new BackgroundPanel("assets/library.jpg", 0.3f);
        background.setBackground(Color.WHITE);
        student.setContentPane(background);
        student.setTitle("PUP Library System/student/");
        student.setSize(600,600);
        student.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        student.setResizable(false);

        //PUP Logo
        ImageIcon logo = new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Container for top elements
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(32, 10, 10, 10));

        //Title text
        JLabel title = new JLabel("PUP Library Student Module");
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

        //User Guide for format
        JLabel guide = new JLabel("e.g., 2023-xxxxx-MN-0");
        guide.setFont(new Font("Roboto", Font.PLAIN, 10));
        guide.setHorizontalAlignment(SwingConstants.CENTER);
        guide.setAlignmentX(Component.CENTER_ALIGNMENT);
        guide.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 370));

        //Input box for Student Number
        JTextField studentID = new JTextField("Student Number");
        studentID.setForeground(Color.GRAY); 
        studentID.setFont(new Font("Roboto", Font.PLAIN, 15));
        studentID.setPreferredSize(new Dimension(500, 40));
        studentID.addFocusListener(new FocusAdapter() { 
            @Override
            public void focusGained(FocusEvent e)
            {
                if (studentID.getText().equals("Student Number"))
                {
                    studentID.setText("");
                    studentID.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e)
            {
                if (studentID.getText().isEmpty())
                {
                    studentID.setText("Student Number");
                    studentID.setForeground(Color.GRAY);
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

        String[] months = {
            "Birth Month", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        JComboBox<String> monthInput = new JComboBox<>(months);
        monthInput.setFont(new Font("Roboto", Font.PLAIN, 13));
        monthInput.setBackground(Color.WHITE);
        monthInput.setForeground(Color.BLACK);
        monthInput.setSelectedIndex(0);
        monthInput.setPreferredSize(new Dimension(202, 40));
        monthInput.getSelectedItem();

        String[] days = new String[32];
        days[0] = "Birth Day";
        for (int i = 1; i < 32; i++)
        {
            days[i] = String.valueOf(i);
        }
        JComboBox<String> dayInput = new JComboBox<>(days);
        dayInput.setFont(new Font("Roboto", Font.PLAIN, 13));
        dayInput.setBackground(Color.WHITE);
        dayInput.setForeground(Color.BLACK);
        dayInput.setSelectedIndex(0);
        dayInput.setPreferredSize(new Dimension(100, 40));
        dayInput.getSelectedItem();

        String[] years = new String[117];
        years[0] = "Birth Year";
        int index = 1;
        for (int i = 2015; i >= 1900; i--, index++)
        {
            years[index] = String.valueOf(i);
        }

        
        JComboBox<String> yearInput = new JComboBox<>(years);
        yearInput.setFont(new Font("Roboto", Font.PLAIN, 13));
        yearInput.setBackground(Color.WHITE);
        yearInput.setForeground(Color.BLACK);
        yearInput.setSelectedIndex(0);
        yearInput.setPreferredSize(new Dimension(150, 40));
        yearInput.getSelectedItem();


        Font buttonFont = new Font("Poppins", Font.PLAIN, 18);
        Dimension buttonSize = new Dimension(500, 50);

        JButton sign_in = Main.createStyledButton("Sign in", buttonFont, buttonSize);
        sign_in.setBackground(Color.decode("#257cf6"));
        sign_in.setAlignmentX(Component.CENTER_ALIGNMENT);
        UIManager.put("OptionPane.background", Color.decode("#dc3546"));
        UIManager.put("Panel.background", Color.decode("#dc3546"));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        sign_in.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                student_number = studentID.getText();
                String birthdate = yearInput.getSelectedItem() + "-" +
                String.format("%02d", monthInput.getSelectedIndex()) + "-" +
                String.format("%02d", Integer.parseInt((String) dayInput.getSelectedItem()));
                student_password = String.valueOf(password.getPassword());

                boolean isPasswordPlaceholder = student_password.equals("Password");
                boolean isIDPlaceholder = student_number.equals("Student Number");

                boolean idEmpty = student_number.isEmpty() || isIDPlaceholder;
                boolean passwordEmpty = student_password.isEmpty() || isPasswordPlaceholder;

                boolean isMonthPlaceholder = monthInput.getSelectedItem().equals("Birth Month");
                boolean isDayPlaceholder = dayInput.getSelectedItem().equals("Birth Day");
                boolean isYearPlaceholder = yearInput.getSelectedItem().equals("Birth Year");

                if (idEmpty && passwordEmpty) 
                {
                    JOptionPane.showMessageDialog
                    (
                        student,
                        "The Student No. field is required.\n\nThe Password field is required.",
                        "Alert!", JOptionPane.ERROR_MESSAGE
                    );
                } 
                else if (idEmpty) 
                {
                    JOptionPane.showMessageDialog
                    (
                        student,
                        "The Student No. field is required.",
                        "Alert!", JOptionPane.ERROR_MESSAGE
                    );
                } 
                else if (passwordEmpty) 
                {
                    JOptionPane.showMessageDialog
                    (
                        student,
                        "The Password field is required.",
                        "Alert!", JOptionPane.ERROR_MESSAGE
                    );
                }
                else if (isMonthPlaceholder || isDayPlaceholder || isYearPlaceholder)
                {
                    JOptionPane.showMessageDialog
                    (
                        student,
                        "Please select a valid birth date before signing in.",
                        "Alert!", JOptionPane.WARNING_MESSAGE
                    );
                }
                else
                {
                    try{
                        Connection con = DBConnection.connect();
                        if(con != null){
                            PreparedStatement stmt = con.prepareStatement(
                                "SELECT * FROM student_login WHERE username=? and birthdate=? and pass=?"
                            );
                            stmt.setString(1, student_number);
                            stmt.setString(2, birthdate);
                            stmt.setString(3, student_password);

                            ResultSet rs = stmt.executeQuery();

                            if (rs.next()) {
                                JOptionPane.showMessageDialog(student, "✅ Login successful!");
                                student.setVisible(false);
                                StudentLibrary.studLibrary(student);
                            } else {
                                login_attempt ++;
                                int attemptsRemaining = 3 - login_attempt;
                                JOptionPane.showMessageDialog
                                (
                                    student,
                                    "Incorrect login credentials (Attempt/s remaining: " + attemptsRemaining + ")",
                                    "Alert!", JOptionPane.ERROR_MESSAGE
                                );
                                if (login_attempt >= 3)
                                {
                                    JOptionPane.showMessageDialog
                                    (
                                        student, "Oops! Maximum log in attempts exceeded.\nClosing program.",
                                        "Alert!", JOptionPane.ERROR_MESSAGE
                                    );
                                    System.exit(0);
                                }
                            }

                            rs.close();
                            stmt.close();
                            con.close();
                        }else{
                            JOptionPane.showMessageDialog(student, "🚫 Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch(Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(student, "⚠️ An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
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

        //Container to store elements horizontally
        JPanel birth = new JPanel();
        birth.setPreferredSize(new Dimension(500, 40));
        birth.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        birth.setOpaque(false);
        birth.add(monthInput);
        birth.add(Box.createHorizontalStrut(16));
        birth.add(dayInput);
        birth.add(Box.createHorizontalStrut(15));
        birth.add(yearInput);
        

        //Container to store elements vertically
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 70, 50));
        loginPanel.add(guide);
        loginPanel.add(studentID);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(birth);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(password);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(sign_in);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(footnote);

        topPanel.add(imageLabel);
        topPanel.add(Box.createVerticalStrut(10)); 
        topPanel.add(title);
        topPanel.add(instruct);

        student.setLayout(new BorderLayout());
        student.add(topPanel, BorderLayout.NORTH);
        student.add(loginPanel, BorderLayout.SOUTH);
        student.setLocationRelativeTo(null);
        student.setVisible(true);
    }
}
