import java.util.Base64;

public class Constants {
    public static final String PROTOCOL_VERSION = "STFMP/1.0";
    public static final String[] ACTIONS = {"write","view","close"};

    public static String ENCRYPT(String rawSting,int key){
        String b64encoded = Base64.getEncoder().encodeToString(rawSting.getBytes());
        StringBuilder tmp = new StringBuilder();
        for(int i = 0; i < b64encoded.length() ; i++){
            tmp.append((char)(b64encoded.charAt(i)+key));
        }
        String encrypt = tmp.toString();
        return encrypt+"\r\n";
    }

    public static String DECRYPT(String encryptedString, int key){
        StringBuilder tmp = new StringBuilder();
        for(int i = 0; i < encryptedString.length() ; i++){
            tmp.append((char)(encryptedString.charAt(i)-key));
        }

        String encrypted = tmp.toString();

        String rawString = new String(Base64.getDecoder().decode(encrypted));

        return rawString+"\r\n";
    }
}
