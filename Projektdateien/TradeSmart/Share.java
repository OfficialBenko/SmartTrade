import java.util.Base64;

public class Share {
    
    private String identifier;
    private String symbol;
    private Double amount;
    private Double price;
    
    
    // Konstruktor
    
    public Share(String identifier, String symbol, Double amount, Double price) {
        this.identifier = identifier;
        this.symbol = symbol;
        this.amount = amount;
        this.price = price;
    }
    
    public Share(String share_string) {
        String plain_string = b64Decode(share_string);
        String[] values = plain_string.split(":");
        this.identifier = b64Decode(values[0]);
        this.symbol = b64Decode(values[1]);
        this.amount = Double.parseDouble(b64Decode(values[2]));
        this.price = Double.parseDouble(b64Decode(values[3]));
    }
    
    
    
    // Verschlüsselte Share-Daten erhalten
    public String getEncryptedData() {
        String share_data = b64Encode(this.identifier) + ":"
                         + b64Encode(this.symbol) + ":"
                         + b64Encode(this.amount.toString()) + ":"
                         + b64Encode(this.price.toString());
        return b64Encode(share_data);
    }
    
    
    // Getter & Setter
    
    private String b64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
    
    private String b64Decode(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }
    
    public String getIdentifier() { 
        return this.identifier; 
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
   
    public String getSymbol() { 
        return this.symbol; 
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public Double getAmount() {
        return this.amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public Double getPrice() {
        return this.price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
}
