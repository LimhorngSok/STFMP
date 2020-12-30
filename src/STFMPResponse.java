import java.util.Map;

public class STFMPResponse {
    private int statusCode;
    private String data;
    private Map<String, String> headers;

    public STFMPResponse(int statusCode, String data, Map<String,String> headers){
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
    }

    public String toString(){
        String statusLine = "STFMP/1.0##"+statusCode+"##"+data;
        String responseHeaders = "";
        if(headers != null){
            for(Map.Entry<String,String> entry : headers.entrySet()){
                responseHeaders += entry.getKey() + ":" + entry.getValue() + "\r\n";
            }
        }
        return statusLine + responseHeaders;
    }
}
