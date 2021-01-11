import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.*;
import java.math.BigDecimal;
import java.io.BufferedReader;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StockGUI extends JFrame {
    
    private static JPanel stock_panel;
    
    private static JLabel name_label;
    private static JLabel company_label;
    private static JLabel details_label;
    private static JLabel price_label;
    private static JTextField price_field;
    private static JLabel amount_label;
    private static JTextField amount_field;
    private static JLabel costs_label;
    private static JTextField costs_field;
    private static JButton calculate_button;
    private static JButton buy_button;
    
    public StockGUI(String symbol, String company) {
        
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        this.setLayout(null);
        this.setSize(494, 358);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        stock_panel = new JPanel();
        stock_panel.setLayout(null);
        stock_panel.setBounds(-1, 0, 480, 320);
        stock_panel.setBackground(Color.WHITE);
        stock_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        
        name_label = new JLabel();
        name_label.setText(symbol);
        name_label.setBounds(0, 10, 480, 30);
        name_label.setHorizontalAlignment(JTextField.CENTER);
        name_label.setFont(new Font("Verdana", Font.BOLD, 30));
        
        company_label = new JLabel();
        company_label.setText(company);
        company_label.setBounds(0, 45, 480, 30);
        company_label.setHorizontalAlignment(JTextField.CENTER);
        company_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
        details_label = new JLabel();
        details_label.setText("Details ansehen");
        details_label.setBounds(0, 80, 480, 30);
        details_label.setHorizontalAlignment(JTextField.CENTER);
        details_label.setFont(new Font("Verdana", Font.CENTER_BASELINE, 12));
        details_label.setForeground(Color.blue);
        Font details_font = details_label.getFont();
        Map attributes = details_font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        details_label.setFont(details_font.deriveFont(attributes));
        details_label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                // URL festlegen
                final String targetUrl = "https://finance.yahoo.com/quote/" + symbol;
                
                try {
                    
                    // URL im Standart-Browser öffnen
                    Desktop.getDesktop().browse(java.net.URI.create(targetUrl));
                
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        price_label = new JLabel("Preis: ");
        price_label.setBounds(100, 120, 130, 30);
        price_label.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        price_field = new JTextField();
        price_field.setText("0");
        price_field.setBounds(160, 120, 160, 30);
        price_field.setMargin(new Insets(5, 5, 5, 5));
        price_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        amount_label = new JLabel("Menge: ");
        amount_label.setBounds(100, 150, 130, 30);
        amount_label.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        amount_field = new JTextField();
        amount_field.setText("0");
        amount_field.setBounds(160, 150, 160, 30);
        amount_field.setMargin(new Insets(5, 5, 5, 5));
        amount_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        costs_label = new JLabel("Costs: ");
        costs_label.setBounds(100, 200, 130, 30);
        costs_label.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        costs_field = new JTextField();
        costs_field.setEnabled(false);
        costs_field.setText("0 $");
        costs_field.setBounds(160, 200, 160, 30);
        costs_field.setMargin(new Insets(5, 5, 5, 5));
        costs_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        calculate_button = new JButton("Berechnen");
        calculate_button.setFocusable(false);
        calculate_button.setBounds(330, 200, 130, 30);
        calculate_button.setFont(new Font("Verdana", Font.BOLD, 15));
        calculate_button.setForeground(Color.WHITE);
        calculate_button.setBackground(new Color(0x2b4c75));
        calculate_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Kleiner Taschenrechner um Ertrag zu berechnen
                if (price_field.getText().length() == 0 || !isNumeric(price_field.getText())) { price_field.setBackground(Color.RED); return;} else { price_field.setBackground(Color.WHITE); }
                if (amount_field.getText().length() == 0 || !isNumeric(amount_field.getText())) { amount_field.setBackground(Color.RED); return;} else { price_field.setBackground(Color.WHITE); }
                Double result = Double.parseDouble(price_field.getText()) * Double.parseDouble(amount_field.getText());
                costs_field.setText(roundAndFormat(result, 2).toString() + " $");
            }
        });
        
        buy_button = new JButton("Kaufen");
        buy_button.setFocusable(false);
        buy_button.setBounds(160, 240, 160, 50);
        buy_button.setFont(new Font("Verdana", Font.BOLD, 25));
        buy_button.setForeground(Color.BLACK);
        buy_button.setBackground(Color.GREEN);
        buy_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (amount_field.getText().length() == 0 || !isNumeric(amount_field.getText()) || amount_field.getText().equals("0")) { amount_field.setBackground(Color.RED); return;}
                
                // Anzahl & Preis ermitteln
                Double amount = Double.parseDouble(amount_field.getText());
                Double price = StockManager.getPrice(symbol);
                
                // Share kaufen
                StockManager.buyShare(symbol, amount, price);
                dispose();
            }
        });
        
        stock_panel.add(name_label);
        stock_panel.add(company_label);
        stock_panel.add(details_label);
        stock_panel.add(price_label);
        stock_panel.add(price_field);
        stock_panel.add(amount_label);
        stock_panel.add(amount_field);
        stock_panel.add(costs_label);
        stock_panel.add(costs_field);
        stock_panel.add(calculate_button);
        stock_panel.add(buy_button);
        
        this.add(stock_panel);
        
        this.setVisible(true);
    }
    
    
    // Check: Eingabe ist eine Zahl
    private static Boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    
    // Zahlen runden auf Nachkommastellen
    public String roundAndFormat(final double value, final int frac) {
        final java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setMaximumFractionDigits(frac);
        return nf.format(new BigDecimal(value));
    }
}
