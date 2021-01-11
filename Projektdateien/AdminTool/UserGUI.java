import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;

public class UserGUI extends JFrame {
    
    private static JPanel user_panel;
    
    private static JLabel identifier_label;
    private static JTextField identifier_field;
    private static JLabel hash_label;
    private static JTextField hash_field;
    private static JButton cred_button;
    private static JLabel balance_label;
    private static JTextField balance_field;
    private static JCheckBox admin_checkbox;
    private static JCheckBox access_checkbox;
    private static JButton back_button;
    private static JButton confirm_button;
    
    private static JFrame cred_frame;
    private static JPanel cred_panel;
    private static JLabel cred_username_label;
    private static JTextField cred_username_field;
    private static JLabel cred_password_label;
    private static JPasswordField cred_password_field;
    private static JButton cred_back_button;
    private static JButton cred_confirm_button;
    
    public UserGUI(User user, Boolean new_account) {
        
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        this.setLayout(null);
        this.setSize(350, 380);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        user_panel = new JPanel();
        user_panel.setLayout(null);
        user_panel.setBounds(-1, 0, 336, 342);
        user_panel.setBackground(Color.WHITE);
        user_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        
        identifier_label = new JLabel("ID: ");
        identifier_label.setBounds(20, 10, 35, 40);
        identifier_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        identifier_field = new JTextField();
        if(!new_account) { identifier_field.setEnabled(false); }
        identifier_field.setBounds(60, 20, 250, 20);
        identifier_field.setFont(new Font("Verdena", Font.PLAIN, 15));
        identifier_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        identifier_field.setText(user.getIdentifier());
        identifier_field.setForeground(Color.black);
        identifier_field.setBackground(Color.white);
        
        hash_label = new JLabel("Hash: ");
        hash_label.setBounds(140, 50, 55, 40);
        hash_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        hash_field = new JTextField();
        hash_field.setBounds(20, 90, 300, 20);
        hash_field.setFont(new Font("Verdena", Font.PLAIN, 15));
        hash_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        hash_field.setText(user.getHash());
        hash_field.setHorizontalAlignment(JTextField.CENTER);
        hash_field.setBackground(Color.white);
        hash_field.setEditable(false);
        
        cred_button = new JButton("Zugangsdaten ändern");
        cred_button.setBounds(75, 130, 180, 30);
        cred_button.setFont(new Font("Verdena", Font.BOLD, 15));
        cred_button.setBorder(null);
        cred_button.setBackground(Color.black);
        cred_button.setForeground(Color.white);
        cred_button.setFocusable(false);
        cred_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Fenster: Login-Daten festlegen
                showCredWindow();
                cred_button.setEnabled(false);
            }
        });
        
        balance_label = new JLabel("Guthaben (in $):");
        balance_label.setBounds(90, 170, 145, 40);
        balance_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        balance_field = new JTextField();
        balance_field.setBounds(20, 210, 300, 20);
        balance_field.setFont(new Font("Verdena", Font.PLAIN, 15));
        balance_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        balance_field.setText(user.getBalance().toString());
        balance_field.setHorizontalAlignment(JTextField.RIGHT);
        
        admin_checkbox = new JCheckBox("Admin");
        admin_checkbox.setFocusable(false);
        admin_checkbox.setSelected(user.getAdmin());
        admin_checkbox.setFont(new Font("Verdena", Font.PLAIN, 15));
        admin_checkbox.setBackground(Color.white);
        admin_checkbox.setBounds(60, 250, 70, 20);
        
        access_checkbox = new JCheckBox("Zugriff");
        access_checkbox.setFocusable(false);
        access_checkbox.setSelected(user.getAccess());
        access_checkbox.setFont(new Font("Verdena", Font.PLAIN, 15));
        access_checkbox.setBackground(Color.white);
        access_checkbox.setBounds(200, 250, 70, 20);
        
        back_button = new JButton("Abbrechen");
        back_button.setFocusable(false);
        back_button.setBounds(20, 290, 120, 30);
        back_button.setFont(new Font("Verdena", Font.BOLD, 15));
        back_button.setBackground(Color.white);
        back_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        confirm_button = new JButton("Bestätigen");
        confirm_button.setFocusable(false);
        confirm_button.setBounds(150, 290, 160, 30);
        confirm_button.setFont(new Font("Verdena", Font.BOLD, 15));
        confirm_button.setBorder(null);
        confirm_button.setBackground(Color.black);
        confirm_button.setForeground(Color.white);
        confirm_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Nutzerdaten festlegen
                user.setIdentifier(identifier_field.getText());
                user.setHash(hash_field.getText());
                user.setBalance(Double.parseDouble(balance_field.getText()));
                user.setAdmin(isAdmin());
                user.setAccess(hasAccess());
                
                // Check: Neuer Nutzer?
                if(new_account) {
                    // Nutzer hinzufügen
                    UserManager.addUser(user);
                } else {
                    // Nutzer aktualisieren
                    UserManager.updateUser(user);
                }
                
                // Nutzerliste aktualisieren
                AdminTool.updateUserList();
                dispose();
            }
        });
        
        user_panel.add(identifier_label);
        user_panel.add(identifier_field);
        user_panel.add(hash_label);
        user_panel.add(hash_field);
        user_panel.add(cred_button);
        user_panel.add(balance_label);
        user_panel.add(balance_field);
        user_panel.add(admin_checkbox);
        user_panel.add(access_checkbox);
        user_panel.add(back_button);
        user_panel.add(confirm_button);
        
        this.add(user_panel);
        this.setVisible(true);
    }
    
    
    // Fenster: Login-Daten festlegen
    private void showCredWindow() {
        
        cred_frame = new JFrame();
        
        cred_frame.setLayout(null);
        cred_frame.setSize(400, 140);
        cred_frame.setResizable(false);
        cred_frame.setUndecorated(true);
        cred_frame.setAlwaysOnTop(true);
        cred_frame.setLocationRelativeTo(null);
        cred_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        cred_panel = new JPanel();
        cred_panel.setLayout(null);
        cred_panel.setBounds(0, 0, 400, 140);
        cred_panel.setBackground(Color.white);
        cred_panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        cred_username_label = new JLabel("NUTZERNAME:");
        cred_username_label.setBounds(20, 10, 130, 30);
        cred_username_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        cred_username_field = new JTextField();
        cred_username_field.setBounds(150, 10, 200, 30);
        cred_username_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        cred_username_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        cred_password_label = new JLabel("PASSWORT:");
        cred_password_label.setBounds(40, 40, 110, 30);
        cred_password_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        cred_password_field = new JPasswordField();
        cred_password_field.setBounds(150, 40, 200, 30);
        cred_password_field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        cred_password_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        cred_back_button = new JButton("Zurück");
        cred_back_button.setFocusable(false);
        cred_back_button.setBounds(20, 90, 120, 30);
        cred_back_button.setFont(new Font("Verdena", Font.BOLD, 15));
        cred_back_button.setBackground(Color.white);
        cred_back_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cred_button.setEnabled(true);
                cred_frame.dispose();
            }
        });
        
        cred_confirm_button = new JButton("Bestätigen");
        cred_confirm_button.setFocusable(false);
        cred_confirm_button.setBounds(150, 90, 200, 30);
        cred_confirm_button.setFont(new Font("Verdena", Font.BOLD, 15));
        cred_confirm_button.setBorder(null);
        cred_confirm_button.setBackground(Color.black);
        cred_confirm_button.setForeground(Color.white);
        cred_confirm_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Eingabefelder überprüfen
                if (cred_username_field.getText().length() == 0) { cred_username_field.setBackground(new Color(255, 102, 102)); return; }
                else { cred_username_field.setBackground(Color.white); }
                if (cred_password_field.getText().length() == 0) { cred_password_field.setBackground(new Color(255, 102, 102)); return; }
                
                try {
                    
                    // MD5-Hash generieren
                    hash_field.setText(Login.generateHash(cred_username_field.getText(), cred_password_field.getText()));
               
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
                
                cred_button.setEnabled(true);
                cred_frame.dispose();
            }
        });
        
        cred_panel.add(cred_username_label);
        cred_panel.add(cred_username_field);
        cred_panel.add(cred_password_label);
        cred_panel.add(cred_password_field);
        cred_panel.add(cred_back_button);
        cred_panel.add(cred_confirm_button);
        
        cred_frame.add(cred_panel);
        cred_frame.setVisible(true);
    }
    
    
    // Check: Nutzer ist Administrator
    private static Boolean isAdmin() {
        if (admin_checkbox.getSelectedObjects() == null) {
            return false;
        } else {
            return true;
        }
    }
    
    
    // Check: Nutzer hat Zugriff auf Anwendung
    private static Boolean hasAccess() {
        if (access_checkbox.getSelectedObjects() == null) {
            return false;
        } else {
            return true;
        }
    }
}
