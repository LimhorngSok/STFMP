import java.util.Map;

public class STFMPRequest {
    private String action;
    private String filename;
    private String content;
    private Map<String, String> headers;

    public STFMPRequest(String action, String filename, String content, Map<String, String> headers){
        this.action = action;
        this.content = content;
        this.filename = filename;
        this.headers = headers;
    }
    public String toString(){
        String requestLine = "STFMP/1.0##"+action+"##"+filename+"#"+content;
        String requestHeaders = "";
        if(headers != null) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                requestHeaders += entry.getKey() + ":" + entry.getValue() + "\r\n";
            }
        }

        return requestLine + requestHeaders;
    }
}
