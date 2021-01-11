import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class Login extends JFrame {
    
    private static JPanel login_panel;
    
    private static ImageIcon logo;
    private static JLabel icon_label;
    private static JButton exit_button;
    
    private static JLabel identifier_label;
    private static JTextField identifier_field;
    
    private static JLabel username_label;
    private static JTextField username_field;
    private static JLabel password_label;
    private static JPasswordField password_field;
    private static JButton login_button;
    
    private static JLabel response_message;
    
    public Login() {
        
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        this.setLayout(null);
        this.setSize(710, 438);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        login_panel = new JPanel();
        login_panel.setLayout(null);
        login_panel.setBounds(-1, 0, 700, 400);
        login_panel.setBackground(Color.WHITE);
        login_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        
        logo = new ImageIcon("Logo.png");
        icon_label = new JLabel();
        icon_label.setIcon(logo);
        icon_label.setBounds(250, 20, 200, 100);
        
        identifier_label = new JLabel("ID: ");
        identifier_label.setBounds(230, 140, 50, 30);
        identifier_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        identifier_field = new JTextField();
        identifier_field.setBounds(265, 140, 170, 30);
        identifier_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        identifier_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        username_label = new JLabel("NUTZERNAME:");
        username_label.setBounds(130, 200, 130, 30);
        username_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        username_field = new JTextField();
        username_field.setBounds(265, 200, 170, 30);
        username_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        username_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        password_label = new JLabel("PASSWORT:");
        password_label.setBounds(150, 240, 110, 30);
        password_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        password_field = new JPasswordField();
        password_field.setBounds(265, 240, 170, 30);
        password_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        password_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        response_message = new JLabel();
        response_message.setBounds(10, 280, 680, 30);
        response_message.setFont(new Font("Verdana", Font.PLAIN, 16));
        response_message.setHorizontalAlignment(JTextField.CENTER);
        response_message.setForeground(Color.red);
        
        login_button = new JButton("LOGIN");
        login_button.setFocusable(false);
        login_button.setBounds(300, 320, 100, 40);
        login_button.setFont(new Font("Verdana", Font.BOLD, 15));
        login_button.setForeground(Color.WHITE);
        login_button.setBackground(new Color(0x2b4c75));
        login_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Eingabefelder auf Inhalt überprüfen
                if (identifier_field.getText().length() == 0) { response_message.setText("Bitte Identifier eingegeben!"); return;}
                if (username_field.getText().length() == 0) { response_message.setText("Bitte Nutzername eingegeben!"); return;}
                if (password_field.getText().length() == 0) { response_message.setText("Bitte Password eingegeben!"); return;}
                
                try {
                    
                   String identifier = identifier_field.getText();
                   String hash = generateHash(username_field.getText(), password_field.getText());
                   
                   // Anmelden mit Identifier und MD5-Hash
                   login(identifier, hash);
                   
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
       
        login_panel.add(icon_label);
        login_panel.add(identifier_label);
        login_panel.add(identifier_field);
        login_panel.add(username_label);
        login_panel.add(username_field);
        login_panel.add(password_label);
        login_panel.add(password_field);
        login_panel.add(response_message);
        login_panel.add(login_button);
        
        this.add(login_panel);
        this.setVisible(true);
    }
    
    
    // Anmeldeversuch durchführen
    private void login(String identifier, String hash) {
        
        User user = UserManager.getUser(identifier);
        if (user == null) { response_message.setText("Ungültige Logindaten!"); return; }
        
        // Hash validieren
        if (hash.equals(user.getHash())) {
            
            // Zugriff nur für Berechtigte
            if (user.getAccess()) {
                
               // Anwendung initialisieren
               TradeSmart.initiate(user);
               this.dispose();
               
            } else {
               response_message.setText("Dein Account ist deaktiviert worden."); 
            }
        } else {
            response_message.setText("Ungültige Logindaten!");
        }
    }
    
    
    // MD5-Nutzerhash erzeugen
    public static String generateHash(String username, String password) 
    throws NoSuchAlgorithmException {
        
        String plain_credentials = username + ":" + password;
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plain_credentials.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        
        return hash;
    }
}
