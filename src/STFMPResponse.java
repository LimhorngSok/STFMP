import java.util.Map;

import static java.lang.Integer.parseInt;

public class STFMPResponse {
    private String status;
    private String message;
    private String protocolVersion;


    public STFMPResponse(String protocolVersion,String status, String message){
        this.protocolVersion = protocolVersion;
        this.status = status;
        this.message = message;

    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String toString(){
        String responseLine = protocolVersion+"##"+status+"##"+message+"\r\n";

        return responseLine;
    }

    public static STFMPResponse fromRawString(String rawString){
        String[] parts = rawString.split("##");
        String protocolVersion = parts[0];
        String status = parts[1];
        String message = parts[2];

        return new STFMPResponse(protocolVersion,status,message);
    }
}
