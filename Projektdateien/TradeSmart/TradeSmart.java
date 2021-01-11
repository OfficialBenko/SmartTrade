import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TradeSmart extends JFrame {
    
    private static User user;
    
    private static JPanel main_panel;
    
    private static JButton dashboard_menubutton;
    private static JButton search_menubutton;
    private static JButton trades_menubutton;
    
    private static JPanel dashboard_panel;
    private static JLabel balance_label;
    private static JLabel name_label;
    
    private static JPanel search_panel;
    private static JLabel search_label;
    private static JLabel results_label;
    private static JTextField search_bar;
    private static JPanel symblist_panel;
    private static JTable symblist_table;
    private static TableModel symblist_model;
    private static JScrollPane symblist_pane;
    
    private static JPanel trades_panel;
    private static JPanel tradelist_panel;
    private static DefaultTableModel tradelist_model;
    private static JTable tradelist_table;
    private static JScrollPane tradelist_pane;
    
    public TradeSmart() {
          
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        this.setLayout(null);
        this.setSize(734, 518);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        main_panel = new JPanel();
        main_panel.setLayout(null);
        main_panel.setBounds(-1, 0, 720, 480);
        main_panel.setBackground(Color.WHITE);
        main_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        
        dashboard_menubutton = new JButton("DASHBOARD");
        dashboard_menubutton.setFocusable(false);
        dashboard_menubutton.setBounds(0, 0, 240, 40);
        dashboard_menubutton.setFont(new Font("Verdana", Font.BOLD, 15));
        dashboard_menubutton.setForeground(Color.WHITE);
        dashboard_menubutton.setBackground(new Color(0x2b4c75));
        dashboard_menubutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                loadDashboard();
            }
        });
        
        search_menubutton = new JButton("SEARCH");
        search_menubutton.setFocusable(false);
        search_menubutton.setBounds(240, 0, 240, 40);
        search_menubutton.setFont(new Font("Verdana", Font.BOLD, 15));
        search_menubutton.setForeground(Color.WHITE);
        search_menubutton.setBackground(new Color(0x2b4c75));
        search_menubutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSearchPanel();
            }
        });
        
        trades_menubutton = new JButton("TRADES");
        trades_menubutton.setFocusable(false);
        trades_menubutton.setBounds(480, 0, 240, 40);
        trades_menubutton.setFont(new Font("Verdana", Font.BOLD, 15));
        trades_menubutton.setForeground(Color.WHITE);
        trades_menubutton.setBackground(new Color(0x2b4c75));
        trades_menubutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadTradesPanel();
            }
        });
        
        dashboard_panel = new JPanel();
        dashboard_panel.setLayout(null);
        dashboard_panel.setBounds(0, 40, 720, 440);
        dashboard_panel.setBackground(Color.WHITE);
        dashboard_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        dashboard_panel.setVisible(false);
        
        name_label = new JLabel();
        name_label.setText(user.getIdentifier());
        name_label.setBounds(0, 140, 720, 50);
        name_label.setHorizontalAlignment(JTextField.CENTER);
        name_label.setFont(new Font("Verdana", Font.BOLD, 50));
        
        balance_label = new JLabel();
        balance_label.setText(user.getBalance().toString() + " $");
        balance_label.setBounds(0, 200, 720, 50);
        balance_label.setHorizontalAlignment(JTextField.CENTER);
        balance_label.setFont(new Font("Verdana", Font.BOLD, 35));
        
        dashboard_panel.add(name_label);
        dashboard_panel.add(balance_label);
        
        search_panel = new JPanel();
        search_panel.setLayout(null);
        search_panel.setBounds(0, 40, 720, 440);
        search_panel.setBackground(Color.WHITE);
        search_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        search_panel.setVisible(false);
        
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
                 // Symbol-Liste aktualisieren
                 updateSymbolList();
             }
        });
        
        results_label = new JLabel();
        results_label.setText("- Results");
        results_label.setBounds(500, 10, 200, 30);
        results_label.setHorizontalAlignment(JTextField.RIGHT);
        results_label.setFont(new Font("Verdana", Font.BOLD, 15));
        results_label.setBackground(Color.green);
        
        symblist_panel = new JPanel();
        symblist_panel.setLayout(new BorderLayout());
        symblist_panel.setBounds(0, 50, 721, 391);
        
        String column_names[] = {"SYMBOL", "COMPANY"}; 
        symblist_model = new DefaultTableModel(column_names, 0);
        symblist_table = new JTable(symblist_model);
        symblist_table.setEnabled(false);
        symblist_table.getTableHeader().setReorderingAllowed(false);
        symblist_table.getTableHeader().setResizingAllowed(false);
        symblist_table.getTableHeader().setBackground(Color.BLACK);
        symblist_table.getTableHeader().setForeground(Color.WHITE);
        symblist_table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 15));
        symblist_table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.black));
        symblist_table.setFont(new Font("Verdana", Font.PLAIN, 14));
        symblist_table.setBackground(Color.WHITE);
        symblist_table.getColumnModel().getColumn(0).setPreferredWidth(220);
        symblist_table.getColumnModel().getColumn(1).setPreferredWidth(500);
        symblist_table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                // Doppelklick
                if (mouseEvent.getClickCount() == 2) {
                    // Neue StockGUI-Instanz mit angeklicktem Symbol
                    new StockGUI(table.getValueAt(row, 0).toString(), table.getValueAt(row, 1).toString());
                }
            }
        });
        symblist_pane = new JScrollPane(symblist_table);
        symblist_panel.add(symblist_pane, BorderLayout.CENTER);
        
        search_panel.add(search_label);
        search_panel.add(search_bar);
        search_panel.add(results_label);
        search_panel.add(symblist_panel);
        
        trades_panel = new JPanel();
        trades_panel.setLayout(null);
        trades_panel.setBounds(0, 40, 720, 440);
        trades_panel.setBackground(Color.WHITE);
        trades_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        trades_panel.setVisible(false);
        
        tradelist_panel = new JPanel();
        tradelist_panel.setLayout(new BorderLayout());
        tradelist_panel.setBounds(0, 0, 721, 441);
        
        String column_names_trade[] = {"SYMBOL", "MENGE", "KOSTEN"}; 
        tradelist_model = new DefaultTableModel(column_names_trade, 0);
        tradelist_table = new JTable(tradelist_model);
        tradelist_table.setEnabled(false);
        tradelist_table.getTableHeader().setReorderingAllowed(false);
        tradelist_table.getTableHeader().setResizingAllowed(false);
        tradelist_table.getTableHeader().setBackground(Color.BLACK);
        tradelist_table.getTableHeader().setForeground(Color.WHITE);
        tradelist_table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 15));
        tradelist_table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.black));
        tradelist_table.setFont(new Font("Verdana", Font.PLAIN, 14));
        tradelist_table.setBackground(Color.WHITE);
        tradelist_table.getColumnModel().getColumn(0).setPreferredWidth(50);
        tradelist_table.getColumnModel().getColumn(1).setPreferredWidth(200);
        tradelist_table.getColumnModel().getColumn(2).setPreferredWidth(200);
        tradelist_table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                // Doppelklick
                if (mouseEvent.getClickCount() == 2) {
                    // Neue SellGUI-Instanz mit Share-Daten
                    new SellGUI(table.getValueAt(row, 0).toString(), table.getValueAt(row, 1).toString(), table.getValueAt(row, 2).toString());
                }
            }
        });
        tradelist_pane = new JScrollPane(tradelist_table);
        tradelist_panel.add(tradelist_pane, BorderLayout.CENTER);
        
        trades_panel.add(tradelist_panel);
        
        main_panel.add(dashboard_menubutton);
        main_panel.add(search_menubutton);
        main_panel.add(trades_menubutton);
        main_panel.add(dashboard_panel);
        main_panel.add(search_panel);
        main_panel.add(trades_panel);
        
        this.add(main_panel);
        this.setVisible(true);    
    }
    
    
    // Main Method - Einstiegspunkt in der Applikation
    public static void main(String[] args) {
        new Login();
    }
    
    
    // Initialisierung
    public static void initiate(User valid_user) {
        user = valid_user;
        StockManager.setup();
        new TradeSmart();
        loadDashboard();
    }
    
    
    // Ausgewählten Button festlegen
    private static void setButtonSelection(JButton button) {
        dashboard_menubutton.setBackground(new Color(0x2b4c75));
        search_menubutton.setBackground(new Color(0x2b4c75));
        trades_menubutton.setBackground(new Color(0x2b4c75));
        button.setBackground(new Color(0x4795bf));
    }
    
    
    // Ausgewähltes Panel festlegen
    private static void setPanelSelection(JPanel panel) {
        dashboard_panel.setVisible(false);
        search_panel.setVisible(false);
        trades_panel.setVisible(false);
        results_label.setText("- Results");
        panel.setVisible(true);
    }
    
    
    // Check: Eingabe ist eine Nummer
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    
    // Check: Eintrag existiert bereits
    public static boolean existsInTable(JTable table, Object[] entry) {

        int rowCount = table.getRowCount();
        int colCount = table.getColumnCount();
        
        String curEntry = "";
        for (Object o : entry) {
            String e = o.toString();
            curEntry = curEntry + " " + e;
        }

        for (int i = 0; i < rowCount; i++) {
            String rowEntry = "";
            for (int j = 0; j < colCount; j++)
                rowEntry = rowEntry + " " + table.getValueAt(i, j).toString();
            if (rowEntry.equalsIgnoreCase(curEntry)) {
                return true;
            }
        }
        return false;
    }
    
    
    // Symbol-Liste aktualisieren
    public static void updateSymbolList() {
        
        String search_regex = search_bar.getText();
        ArrayList<JSONObject> symblist = StockManager.findSymbols(search_regex);
        DefaultTableModel model = (DefaultTableModel) symblist_table.getModel();
        model.setRowCount(0);
        
        for (int i = 0; i < symblist.size(); i++) {
            JSONObject symbject = (JSONObject)symblist.get(i);
            String symbol = (String) symbject.get("symbol");
             
            // Symbol: Format-Anpassung
            if (symbol.contains(".")) {
                if (symbol.indexOf(".") != -1) {
                    if (isNumeric(symbol.substring(0, symbol.indexOf(".")))) { continue; }
                    symbol = symbol.substring(0, symbol.indexOf("."));
                }
            }
            
            String name = (String) symbject.get("name");
            if(!existsInTable(symblist_table, new Object[] { symbol, name })) {
                // Tabellenreihe mit Aktien-Daten einfügen
                model.insertRow(model.getRowCount(), new Object[] { symbol, name });
            }
        }
        // Anzahl an passenden Einträgen anzeigen
        results_label.setText(model.getRowCount() + " Results");
    }
     
    
    // Share-Liste aktualisieren
    public static void updateShareList() {
        
        // Alle Share-Daten des Nutzers speichern
        ArrayList<String> sharelist = StockManager.getUserShares(user);
        
        DefaultTableModel model = (DefaultTableModel) tradelist_table.getModel();
        model.setRowCount(0);
        
        for (int i = 0; i < sharelist.size(); i++) {
            Share share = new Share(sharelist.get(i));
            // Tabellenreihe mit Share-Daten des Nutzers einfügen
            model.insertRow(model.getRowCount(), new Object[] { share.getSymbol(), share.getAmount(), share.getPrice() });
        }
    }
    
    
    // Dashboard laden
    private static void loadDashboard() {
        setButtonSelection(dashboard_menubutton);
        setPanelSelection(dashboard_panel);
        name_label.setText(user.getIdentifier());
        balance_label.setText(user.getBalance().toString() + " $");
    }
    
    
    // SearchPanel laden
    private static void loadSearchPanel() {
        setButtonSelection(search_menubutton);
        setPanelSelection(search_panel);
        updateSymbolList();
    }
    
    
    // TradesPanel laden
    private static void loadTradesPanel() {
        setButtonSelection(trades_menubutton);
        setPanelSelection(trades_panel);
        updateShareList();
    }
    
    
    // Aktiven Nutzer ermitteln
    public static User getActiveUser() {
        return user;
    }
}
