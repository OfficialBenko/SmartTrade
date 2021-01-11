import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserManager {
    
    
    // Initialisierung
    public static void initiate() {
        createFile("users.txt");
        // Standart Logindaten für Administrator - Identifier: Admin & Username: Admin & Password: Admin
        addUser(new User("Admin", "3E8C124A2FB3F607596C6BEF707594F1", 100000.0, true, true));
    }
    
    
    // Nutzer hinzufügen
    public static Boolean addUser(User user) {
        FileWriter file_writer;
        if (userExists(user)) {
            return false;
        }
        try {
            file_writer = new FileWriter("users.txt", true);
            file_writer.append(user.getEncryptedData() + "\n");
            file_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    
    // Nutzer(-daten) erhalten
    public static User getUser(String identifier) {
       ArrayList<String> user_strings = getUserStrings();
       for (int i = 0; i < user_strings.size(); i++) {
           User compare_user = new User(user_strings.get(i));
           if (identifier.equals(compare_user.getIdentifier())) {
               return compare_user;
           }
       }
       return null; 
    }
    
    
    // Alle Nutzer(-daten) erhalten
    public static ArrayList<String> getAllUsers() {
        return getUserStrings();
    }
    
    
    // Nutzer(-daten) aktualisieren
    public static void updateUser(User user) {
        
        try {      
            File dataFile = new File("users.txt");
            
            // Temporäre Datei erstellen
            File tempFile = new File("temp.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String lineToUpdate = getUser(user.getIdentifier()).getEncryptedData();
            String currentLine;
            
            // Zeile für Zeile überprüfen & übertragen
            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if(trimmedLine.equals(lineToUpdate)) {
                    // Aktualisierte Nutzerdaten einfügen
                    writer.write(user.getEncryptedData() + "\n");
                    continue;
                }
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

    
    // Nutzer entfernen 
    public static void removeUser(User user) {
        
        try {
            File dataFile = new File("users.txt");
            
            // Temporäre Datei erstellen
            File tempFile = new File("temp.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String lineToRemove = user.getEncryptedData();
            String currentLine;
            
            // Zeile für Zeile überprüfen & übertragen
            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                // Zeile mit Nutzerdaten überspringen
                if(trimmedLine.equals(lineToRemove)) continue;
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
    
    
    // Nutzerexistens überprüfen
    private static Boolean userExists(User user) {
       ArrayList<String> user_strings = getUserStrings();
       for (int i = 0; i < user_strings.size(); i++) {
           User compare_user = new User(user_strings.get(i));
           if (user.getIdentifier().equals(compare_user.getIdentifier())) {
               return true;
           }
       }
       return false; 
    }
    
    
    // Alle verschlüsselten Nutzer(-daten) erhalten
    private static ArrayList<String> getUserStrings() {
        ArrayList<String> user_strings = new ArrayList<String>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("users.txt"));
            String line = reader.readLine();
            while (line != null) {
                user_strings.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user_strings;
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
}
