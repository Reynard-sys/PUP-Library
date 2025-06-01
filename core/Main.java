import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;


//Method to lower opacity of background image
class BackgroundPanel extends JPanel
{
    private Image image;
    private float opacity;

    public BackgroundPanel(String imageLoc, float opacity)
    {
        this.opacity = opacity;

        ImageIcon icon = new ImageIcon(imageLoc);
        image = icon.getImage();
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(image != null)
        {
            Graphics2D g2d = (Graphics2D) g.create();
            
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}

public class Main
{
    public static void main(String[] args)
    {
        //Main Frame Creation
        JFrame landing = new JFrame();
        BackgroundPanel background = new BackgroundPanel("assets/library.jpg", 0.3f);
        background.setBackground(Color.WHITE);
        landing.setContentPane(background);
        landing.setTitle("PUP Library System");
        landing.setSize(600,600);
        landing.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        landing.setResizable(false);

        //PUP Logo
        ImageIcon logo = new ImageIcon(new ImageIcon("assets/pup.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Container for the elements on top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(32, 10, 10, 10));
        
        //Title Text
        JLabel title = new JLabel("PUP Library System");
        title.setFont(new Font("Poppins", Font.BOLD, 35));
        title.setBorder((BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        title.setForeground(Color.decode("#700000"));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Sub-Title
        JLabel greet = new JLabel("Hi, PUPian!");
        greet.setFont(new Font("Roboto", Font.BOLD, 25));
        greet.setBorder((BorderFactory.createEmptyBorder(20, 10, 10, 10)));
        greet.setHorizontalAlignment(SwingConstants.CENTER);
        greet.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Sub-Text
        JLabel instruct = new JLabel("🡻 Please click or tap your destination.");
        instruct.setFont(new Font("Roboto", Font.PLAIN, 20));
        instruct.setBorder((BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        instruct.setHorizontalAlignment(SwingConstants.CENTER);
        instruct.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Container for the button of Student and Faculty
        JPanel type = new JPanel();
        type.setLayout(new BoxLayout(type, BoxLayout.Y_AXIS));
        type.setOpaque(false);
        type.setBorder(BorderFactory.createEmptyBorder(100, 50, 110, 50));

        //Default font and button size for buttons
        Font buttonFont = new Font("Poppins", Font.PLAIN, 18);
        Dimension buttonSize = new Dimension(500, 50);

        //Student and Faculty Button Creation
        JButton student = createStyledButton("Student", buttonFont, buttonSize);
        student.setBackground(Color.decode("#257cf6"));
        student.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton faculty = createStyledButton("Faculty", buttonFont, buttonSize);
        faculty.setBackground(Color.decode("#d63f4a"));
        faculty.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Hover effect to Student Button
        student.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                student.setBackground(Color.decode("#0D47A1"));
            }
            public void mouseExited(MouseEvent e) {
                student.setBackground(Color.decode("#257cf6"));
            }
        });

        //Hover effect to Faculty Button
        faculty.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                faculty.setBackground(Color.decode("#B71C1C"));
            }
            public void mouseExited(MouseEvent e) {
                faculty.setBackground(Color.decode("#d63f4a"));
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
        
        //When buttons clicked, it will go to the next page
        student.addActionListener(e -> {
            landing.setVisible(false);
            StudentLogin.window(landing);
        });
        faculty.addActionListener(e -> {
            landing.setVisible(false);
            FacultyLogin.window(landing);
        });

        //When PUP logo clicked, it will return to the landing page
        student.addActionListener(e -> {
            landing.dispose();
            new StudentLogin();
        });
        faculty.addActionListener(e -> {
            landing.dispose();
            new FacultyLogin();
        });

        //Adding of elements in the type container
        type.add(student);
        type.add(Box.createVerticalStrut(20));
        type.add(faculty);
        type.add(Box.createVerticalStrut(30));
        type.add(footnote);

        //Adding of elements in the topPanel container
        topPanel.add(imageLabel);
        topPanel.add(Box.createVerticalStrut(10)); 
        topPanel.add(title);
        topPanel.add(greet);
        topPanel.add(instruct);
        
        //Finalizing the main frame
        landing.setLayout(new BorderLayout());
        landing.add(topPanel, BorderLayout.NORTH);
        landing.add(type, BorderLayout.SOUTH);
        landing.setLocationRelativeTo(null);
        landing.setVisible(true);
    }

    //Default button properties
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
}