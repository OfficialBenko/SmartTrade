import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import javax.swing.*;
import java.util.*;

public class SellGUI extends JFrame {
    
    private static JPanel sell_panel;
    private static JLabel info_label;
    private static JLabel name_label;
    private static JLabel details_label;
    private static JLabel price_label;
    private static JTextField price_field;
    private static JLabel total_label;
    private static JTextField total_field;
    private static JLabel dif_label;
    private static JTextField dif_field;
    private static JButton calculate_button;
    private static JButton sell_button;
    
    public SellGUI(String symbol, String amount, String costs) {
       
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        this.setLayout(null);
        this.setSize(494, 358);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        sell_panel = new JPanel();
        sell_panel.setLayout(null);
        sell_panel.setBounds(-1, 0, 480, 320);
        sell_panel.setBackground(Color.WHITE);
        sell_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        
        name_label = new JLabel();
        name_label.setText(symbol);
        name_label.setBounds(0, 10, 480, 30);
        name_label.setHorizontalAlignment(JTextField.CENTER);
        name_label.setFont(new Font("Verdana", Font.BOLD, 30));
        
        info_label = new JLabel();
        info_label.setText((int)((double) Double.parseDouble(amount)) + " Aktien für " + costs + " $");
        info_label.setBounds(0, 45, 480, 30);
        info_label.setHorizontalAlignment(JTextField.CENTER);
        info_label.setFont(new Font("Verdana", Font.BOLD, 15));
        
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
        
        total_label = new JLabel("Summe: ");
        total_label.setBounds(78, 170, 130, 30);
        total_label.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        total_field = new JTextField();
        total_field.setEnabled(false);
        total_field.setText("0 $");
        total_field.setBounds(160, 170, 160, 30);
        total_field.setMargin(new Insets(5, 5, 5, 5));
        total_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        total_field.setBackground(Color.WHITE);
        total_field.setForeground(Color.BLACK);
        
        dif_label = new JLabel("Gewinn: ");
        dif_label.setBounds(80, 200, 130, 30);
        dif_label.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        dif_field = new JTextField();
        dif_field.setEnabled(false);
        dif_field.setText("0 $");
        dif_field.setBounds(160, 200, 160, 30);
        dif_field.setMargin(new Insets(5, 5, 5, 5));
        dif_field.setFont(new Font("Verdana", Font.PLAIN, 15));
        dif_field.setBackground(Color.WHITE);
        dif_field.setForeground(Color.BLACK);
        
        calculate_button = new JButton("Berechnen");
        calculate_button.setFocusable(false);
        calculate_button.setBounds(330, 200, 130, 30);
        calculate_button.setFont(new Font("Verdana", Font.BOLD, 15));
        calculate_button.setForeground(Color.WHITE);
        calculate_button.setBackground(new Color(0x2b4c75));
        calculate_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Kleiner Taschenrechner um Preis zu berechnen
                if (price_field.getText().length() == 0 || !isNumeric(price_field.getText())) { price_field.setBackground(Color.RED); return;} else { price_field.setBackground(Color.WHITE); }
                Double sum = Double.parseDouble(amount) * Double.parseDouble(price_field.getText());
                Double dif = sum - Double.parseDouble(costs); 
                total_field.setText(roundAndFormat(sum, 2).toString() + " $");
                dif_field.setText(roundAndFormat(dif, 2).toString() + " $");
            }
        });
        
        sell_button = new JButton("Verkaufen");
        sell_button.setFocusable(false);
        sell_button.setBounds(150, 240, 180, 50);
        sell_button.setFont(new Font("Verdana", Font.BOLD, 25));
        sell_button.setForeground(Color.BLACK);
        sell_button.setBackground(Color.RED);
        sell_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                // Nutzer & Share ermitteln
                User user = TradeSmart.getActiveUser();
                Share share = new Share(user.getIdentifier(), symbol, Double.parseDouble(amount), Double.parseDouble(costs));
                
                // Share verkaufen
                StockManager.sellShare(share);
                dispose();
            }
        });
        
        sell_panel.add(name_label);
        sell_panel.add(info_label);
        sell_panel.add(details_label);
        sell_panel.add(price_label);
        sell_panel.add(price_field);
        sell_panel.add(total_label);
        sell_panel.add(total_field);
        sell_panel.add(dif_label);
        sell_panel.add(dif_field);
        sell_panel.add(calculate_button);
        sell_panel.add(sell_button);
        
        this.add(sell_panel);
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
