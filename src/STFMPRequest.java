import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class STFMPRequest {
    private String action;
    private String filename;
    private String content;
    private String protocolVersion;
    private String params;


    public STFMPRequest(String protocolVersion,String action, String filename, String content){
        this.protocolVersion = protocolVersion;
        this.action = action;
        this.content = content;
        this.filename = filename;

    }

    public String getAction() {
        return action;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String getParams() {
        return params;
    }

    public String toString(){
        String requestLine = protocolVersion+"##"+action+"##"+filename+"#"+content+"\r\n";

        return requestLine;
    }



    public static STFMPRequest fromRawString(String rawString){
       String[] parts = rawString.split("##");
       String protocolVersion = parts[0];
       String action = parts[1];
       String params = parts[2];
       String[] listParams = params.split("#");
       String filename = listParams[0];
       String content = listParams[1];

       return new STFMPRequest(protocolVersion,action,filename,content);
    }
}
