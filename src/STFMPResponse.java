import java.util.Map;

import static java.lang.Integer.parseInt;

public class STFMPResponse {
    private String status;
    private String data;
    private String protocolVersion;


    public STFMPResponse(String protocolVersion,String status, String data){
        this.protocolVersion = protocolVersion;
        this.status = status;
        this.data = data;

    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return data;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String rawResponse(){
        String responseLine = protocolVersion+"##"+status+"##"+data+"\r\n";
        return responseLine;
    }

    public static STFMPResponse decryptResponse(String encryptedResponse, int key){
        String decryptedResponse = Constants.DECRYPT(encryptedResponse, key);
        System.out.println("Decrypting: "+decryptedResponse);
        String[] parts = decryptedResponse.split("##");
        String protocolVersion = parts[0];
        String status = parts[1];
        String data = parts[2];

        return new STFMPResponse(protocolVersion,status,data);
    }
}
