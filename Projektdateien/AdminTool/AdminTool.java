import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.regex.*;

public class AdminTool extends JFrame {
    
    private static User user;
    
    private static JPanel admin_panel;
    private static JPanel userlist_panel;
    
    private static JTable userlist_table;
    private static TableModel userlist_model;
    private static JScrollPane userlist_pane;
    
    private static JLabel search_label;
    private static JTextField search_bar;
    
    private static JButton adduser_button;
    private static JButton removeuser_button;
    
    public AdminTool() {
        
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        this.setLayout(null);
        this.setSize(814, 510);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        admin_panel = new JPanel();
        admin_panel.setLayout(null);
        admin_panel.setBounds(-1, 0, 800, 500);
        admin_panel.setBackground(Color.WHITE);
        admin_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        search_label = new JLabel("Suche:");
        search_label.setBounds(10, 10, 60, 30);
        search_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        search_bar = new JTextField();
        search_bar.setBounds(80, 10, 200, 30);
        search_bar.setFont(new Font("Verdana", Font.PLAIN, 15));
        search_bar.setMargin(new Insets(5,5,5,5));
        search_bar.getDocument().addDocumentListener(new DocumentListener() {
             public void changedUpdate(DocumentEvent e) { perform_update(); }
             public void insertUpdate(DocumentEvent e) { perform_update(); }
             public void removeUpdate(DocumentEvent e) { perform_update(); }
             private void perform_update() {
                 // Nutzerliste aktualisieren
                 updateUserList();
             }
        });
        
        removeuser_button = new JButton("Entfernen");
        removeuser_button.setFocusable(false);
        removeuser_button.setBounds(290, 10, 110, 30);
        removeuser_button.setFont(new Font("Verdena", Font.BOLD, 15));
        removeuser_button.setBackground(new Color(0xE76F51));
        removeuser_button.setForeground(Color.white);
        removeuser_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (UserManager.getUser(search_bar.getText()) != null) {
                    // Nutzer entfernen
                    UserManager.removeUser(UserManager.getUser(search_bar.getText()));
                    // Nutzerliste aktualisieren
                    updateUserList();
                }
            }
        });
        
        adduser_button = new JButton("Neuer Nutzer");
        adduser_button.setFocusable(false);
        adduser_button.setBounds(650, 10, 140, 30);
        adduser_button.setFont(new Font("Verdena", Font.BOLD, 15));
        adduser_button.setBackground(new Color(0x2A9D8F));
        adduser_button.setForeground(Color.white);
        adduser_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Neuen Nutzer erstellen & neue UserGUI-Instanz
                User new_user = new User("", "", 0.0, false, false);
                new UserGUI(new_user, true);
            }
        });
        
        userlist_panel = new JPanel();
        userlist_panel.setLayout(new BorderLayout());
        userlist_panel.setBounds(0, 50, 800, 423);
        
        String column_names[] = {"ID", "HASH", "GUTHABEN ($)", "ADMIN", "ZUGRIFF"}; 
        userlist_model = new DefaultTableModel(column_names, 0);
        userlist_table = new JTable(userlist_model);
        userlist_table.setEnabled(false);
        userlist_table.getTableHeader().setReorderingAllowed(false);
        userlist_table.getTableHeader().setResizingAllowed(false);
        userlist_table.getTableHeader().setBackground(new Color(0x264653));
        userlist_table.getTableHeader().setForeground(Color.white);
        userlist_table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 15));
        userlist_table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.black));
        userlist_table.setFont(new Font("Verdana", Font.PLAIN, 14));
        userlist_table.setBackground(Color.white);
        userlist_table.getColumnModel().getColumn(0).setPreferredWidth(100);
        userlist_table.getColumnModel().getColumn(1).setPreferredWidth(220);
        userlist_table.getColumnModel().getColumn(2).setPreferredWidth(80);
        userlist_table.getColumnModel().getColumn(3).setPreferredWidth(5);
        userlist_table.getColumnModel().getColumn(4).setPreferredWidth(5);
        userlist_table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                // Doppelklick
                if (mouseEvent.getClickCount() == 2) {
                    // Neue UserGUI-Instanz mit angeklicktem Nutzer
                    new UserGUI(UserManager.getUser(table.getValueAt(row, 0).toString()), false);
                }
            }
        });
        userlist_pane = new JScrollPane(userlist_table);
        
        userlist_panel.add(userlist_pane, BorderLayout.CENTER);
        
        admin_panel.add(search_label);
        admin_panel.add(search_bar);
        admin_panel.add(removeuser_button);
        admin_panel.add(adduser_button);
        admin_panel.add(userlist_panel);
        
        this.add(admin_panel);
        this.setVisible(true);
    }
    
    
    // Main Method - Einstiegspunkt in der Applikation
    public static void main(String[] args) {
        UserManager.initiate();
        new Login();
    }
    
    
    // Initialisierung
    public static void initiate(User validated_user) {
        user = validated_user;
        new AdminTool();
        updateUserList();
    }
    
    
    // Nutzerliste aktualisieren
    public static void updateUserList() {
        
        String search_regex = search_bar.getText();
        ArrayList<String> user_list = UserManager.getAllUsers();
        DefaultTableModel model = (DefaultTableModel) userlist_table.getModel();
        model.setRowCount(0);
        
        for (int i = 0; i < user_list.size(); i++) {
            User user = new User(user_list.get(i));
            if (!Pattern.matches("^"+ search_regex + ".*", user.getIdentifier())) { continue; }
            // Tabellenreihe mit Nutzerdaten einfügen
            model.insertRow(model.getRowCount(), new Object[] { user.getIdentifier(),
                                              user.getHash(),
                                              user.getBalance(),
                                              user.getAdmin(),
                                              user.getAccess() });
        }
    }
}
