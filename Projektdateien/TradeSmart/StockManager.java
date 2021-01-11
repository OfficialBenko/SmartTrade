import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.text.*;
import java.net.URL;

public class StockManager {
    
    // JSONArray mit allen Symbolen
    private static JSONArray symbarr;
    
    
    // StockManager einrichten
    public static void setup() {
        createFile("shares.txt");
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("symblist.json"));
            symbarr = (JSONArray)obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Passende Symbole finden 
    public static ArrayList<JSONObject> findSymbols(String symbol_regex) {
        
        ArrayList<JSONObject> symbols = new ArrayList<JSONObject>();
        String regex = "^" + symbol_regex.toLowerCase() + ".*";
        
        for (int i = 0; i < symbarr.size(); i++) {
            
            JSONObject symbject = (JSONObject)symbarr.get(i);
            if (!(symbject.size() == 4)) { continue; }
            String symbol = (String) symbject.get("symbol");
            String name = (String) symbject.get("name");
            
            // Check: Symbol passt zur Eingabe
            if ((symbol.toLowerCase().matches(regex) || name.toLowerCase().matches(regex)) && !symbols.contains(symbject))  {
                // Symbol zur Liste hinzufügen
                symbols.add(symbject);
                
            }
        }
        return symbols;
    }
    
    
    // Share hinzufügen
    public static Boolean addShare(Share share) {
        FileWriter file_writer;
        try {
            file_writer = new FileWriter("shares.txt", true);
            file_writer.append(share.getEncryptedData() + "\n");
            file_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    
    // Share entfernen
    public static void removeShare(Share share) {
        
        try {
            File dataFile = new File("shares.txt");
            
            // Temporäre Datei erstellen
            File tempFile = new File("temps.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String lineToRemove = share.getEncryptedData();
            String currentLine;
            Boolean found = false;
            
            // Zeile für Zeile überprüfen & übertragen
            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                // Entsprechende Zeile mit Share überspringen
                if(trimmedLine.equals(lineToRemove) && !found) { found = true; continue; }
                writer.write(currentLine + "\n");
            }
            
            writer.close(); 
            reader.close();  
            dataFile.setWritable(true);
            
            // Original-Datei löschen (GarbageCollector)
            while(!dataFile.delete()) { 
                System.gc(); 
                try { 
                    Thread.sleep(500); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // Temporäre Datei zur Original-Datei umbennenen 
            tempFile.renameTo(dataFile);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // Alle Shares eines Nutzers erhalten
    public static ArrayList<String> getUserShares(User user) {
        
        ArrayList<String> user_shares = new ArrayList<String>();
        BufferedReader reader;
        
        try {
            reader = new BufferedReader(new FileReader("shares.txt"));
            String line = reader.readLine();
            
            while (line != null) {
                Share share = new Share(line);
                
                // Check: Share des Nutzers
                if (share.getIdentifier().equals(user.getIdentifier())) {
                    // Share zur Liste hinzufügen
                    user_shares.add(line);
                }
                
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user_shares;
    }
    
    
    // Datei erstellen
    private static void createFile(String file_name) {
        try {
          File file = new File(file_name);
          file.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    
    
    // Share kaufen
    public static void buyShare(String symbol, Double amount, Double price) {
        User user = TradeSmart.getActiveUser();
        Double total = amount * price;
        
        // Check: Nutzer hat genug Guthaben
        if (user.getBalance() >= total) {
            // Kauf-Fenster öffnen - ohne Fehler
            showBuyWindow(symbol, amount, total, true);
        } else {
            // Kauf-Fenster öffnen - mit Fehler
            showBuyWindow(symbol, amount, total, false);
        }
    }
    
    
    // Share verkaufen
    public static void sellShare(Share share) {
        // Verkaufs-Fenster öffnen
        showSellWindow(share);
    } 
    
    
    // Kauf-Fenster öffnen
    private static void showBuyWindow(String symbol, double amount, double costs, Boolean success) {
        
        // Aktiven Nutzer ermitteln
        User user = TradeSmart.getActiveUser();
        
        JFrame buy_window;
        JPanel buy_panel;
        
        JLabel fail_label;
        JLabel balance_label;
        
        JTextPane message_pane;
        JButton confirm_button;
        
        buy_window = new JFrame();
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        buy_window.setIconImage(icon);
        buy_window.setLayout(null);
        buy_window.setSize(364, 238);
        buy_window.setResizable(false);
        buy_window.setLocationRelativeTo(null);
        buy_window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        buy_panel = new JPanel();
        buy_panel.setLayout(null);
        buy_panel.setBounds(-1, 0, 350, 200);
        buy_panel.setBackground(Color.WHITE);
        buy_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        
        fail_label = new JLabel();
        fail_label.setText("Nicht genug Guthaben!");
        fail_label.setBounds(0, 30, 350, 50);
        fail_label.setHorizontalAlignment(JTextField.CENTER);
        fail_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        balance_label = new JLabel();
        balance_label.setText("Aktuelles Guthaben: " + user.getBalance() + " $");
        balance_label.setBounds(0, 80, 350, 50);
        balance_label.setHorizontalAlignment(JTextField.CENTER);
        balance_label.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        message_pane = new JTextPane();
        message_pane.setFocusable(false);
        message_pane.setEditable(false);
        message_pane.setBounds(25, 25, 300, 100);
        message_pane.setText("Sie sind dabei " + (int)amount + " Aktien \nfür " + costs + " $ zu kaufen." 
                              + "\n\nBitte bestätigen Sie den Kauf!");
        message_pane.setFont(new Font("Verdana", Font.PLAIN, 15));
        StyledDocument doc = message_pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        confirm_button = new JButton("BESTÄTIGEN");
        confirm_button.setFocusable(false);
        confirm_button.setBounds(85, 130, 180, 40);
        confirm_button.setFont(new Font("Verdana", Font.BOLD, 20));
        confirm_button.setForeground(Color.BLACK);
        confirm_button.setBackground(Color.GREEN);
        confirm_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Kosten des Shares vom Guthaben abziehen 
                user.setBalance(user.getBalance() - costs);
                UserManager.updateUser(user);
                
                // Neuen Share hinzufügen
                addShare(new Share(user.getIdentifier(), symbol, amount, costs));
                buy_window.dispose();
            }
        });
        
        // Check: Fehlermeldung
        if (success) {
            // Panel-Komponenten ohne Fehler
            buy_panel.add(message_pane);
            buy_panel.add(confirm_button);
        } else {
            // Panel-Komponenten mit Fehler
            buy_panel.add(fail_label);
            buy_panel.add(balance_label);
        }
        
        buy_window.add(buy_panel);
        buy_window.setVisible(true);
    }
    
    
    // Verkaufs-Fenster öffnen
    private static void showSellWindow(Share share) {
        
        // Aktiven Nutzer ermitteln
        User user = TradeSmart.getActiveUser();
        
        JFrame sell_window;
        JPanel sell_panel;
        JTextPane message_pane;
        JButton confirm_button;
        
        sell_window = new JFrame();
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        sell_window.setIconImage(icon);
        sell_window.setLayout(null);
        sell_window.setSize(364, 238);
        sell_window.setResizable(false);
        sell_window.setLocationRelativeTo(null);
        sell_window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        sell_panel = new JPanel();
        sell_panel.setLayout(null);
        sell_panel.setBounds(-1, 0, 350, 200);
        sell_panel.setBackground(Color.WHITE);
        sell_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 

        message_pane = new JTextPane();
        message_pane.setFocusable(false);
        message_pane.setEditable(false);
        message_pane.setBounds(25, 25, 300, 100);
        message_pane.setText("Sie sind dabei " + (int) ((double) share.getAmount()) + " Aktien \nfür " + share.getPrice().toString() + " $ zu verkaufen." 
                              + "\n\nBitte bestätigen Sie den Verkauf!");
        message_pane.setFont(new Font("Verdana", Font.PLAIN, 15));
        StyledDocument doc = message_pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        confirm_button = new JButton("BESTÄTIGEN");
        confirm_button.setFocusable(false);
        confirm_button.setBounds(85, 130, 180, 40);
        confirm_button.setFont(new Font("Verdana", Font.BOLD, 20));
        confirm_button.setForeground(Color.BLACK);
        confirm_button.setBackground(Color.GREEN);
        confirm_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Ertrag des Shares dem Nutzer gutschreiben
                user.setBalance(user.getBalance() + (share.getAmount() * getPrice(share.getSymbol())));
                UserManager.updateUser(user);
                
                // Share entfernen
                removeShare(share);
                
                // Share-Liste aktualisieren
                TradeSmart.updateShareList();
                sell_window.dispose();
            }
        });
        
        sell_panel.add(message_pane);
        sell_panel.add(confirm_button);
        
        sell_window.add(sell_panel);
        sell_window.setVisible(true);
    }
    
    
    // Aktuellen echten Wert eines Shares ermitteln (Real-Time)
    public static Double getPrice(String symbol) {
        
        String response = "";
        Double price = 0.0;
        
        try {
            // URL festlegen (mit API-Key - Limitierung: 250 Anfragen / Tag)
            URL url = new URL("https://financialmodelingprep.com/api/v3/quote-short/" + symbol + "?apikey=278807135dad85c22163df813bed502a");
            
            // Antwort der Website einlesen
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                for (String line; (line = reader.readLine()) != null;) {
                    response += line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Preis ermitteln
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(response);
                JSONArray resparry = (JSONArray)obj;
                JSONObject symbject = (JSONObject)resparry.get(0);
                price = Double.parseDouble(symbject.get("price").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    } 
}
