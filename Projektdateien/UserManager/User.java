import java.util.Base64;

public class User {
    
    private String identifier;
    private String hash;
    private Double balance;
    private Boolean admin;
    private Boolean access;
    
    
    // Konstruktor
    
    public User(String identifier, String hash, Double balance, Boolean admin, Boolean access) {
        this.identifier = identifier;
        this.hash = hash;
        this.balance = balance;
        this.admin = admin;
        this.access = access;
    }
    
    public User(String user_string) {
        String plain_string = b64Decode(user_string);
        String[] values = plain_string.split(":");
        this.identifier = b64Decode(values[0]);
        this.hash = b64Decode(values[1]);
        this.balance = Double.parseDouble(b64Decode(values[2]));
        this.admin = Boolean.parseBoolean(b64Decode(values[3]));
        this.access = Boolean.parseBoolean(b64Decode(values[4]));
    }
    
    
    
    // Verschlüsselte Nutzerdaten erhalten
    public String getEncryptedData() {
        String user_data = b64Encode(this.identifier) + ":"
                         + b64Encode(this.hash) + ":"
                         + b64Encode(this.balance.toString()) + ":"
                         + b64Encode(this.admin.toString()) + ":"
                         + b64Encode(this.access.toString());
        return b64Encode(user_data);
    }
    
    
    // Verschlüsselung
    private String b64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
    
    // Entschlüsselung
    private String b64Decode(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    } 
    
    
    // Getter & Setter
    
    public String getIdentifier() { 
        return this.identifier; 
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
   
    public String getHash() { 
        return this.hash; 
    }
    
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    public Double getBalance() {
        return this.balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
    public Boolean getAdmin() { 
        return this.admin; 
    }
    
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    
    public Boolean getAccess() { 
        return this.access; 
    }
    
    public void setAccess(Boolean access) {
        this.access = access;
    }
}
