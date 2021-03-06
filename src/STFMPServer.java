import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class STFMPServer {
    private static int KEY = 2;
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("Waiting to accept a client");
            Socket connection = serverSocket.accept();

            //Send Encryption Key
            if(KEY == 0){
                sendKey(connection);
            }
            System.out.println("Bind to port 9999");
            while (true){

                System.out.println("Read request from the clients");
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                //Get Encryption
                String encryptedRequest = scanner.nextLine();
                System.out.println("Receiving: "+encryptedRequest);
                //Creating Request
                STFMPRequest request = STFMPRequest.decryptRequest(encryptedRequest);
                System.out.println("Send response to client");

                if(request.getAction().equals(STFMPActions.WRITE)){
                    STFMPWrite(connection,request);
                }else if(request.getAction().equals(STFMPActions.VIEW)){
                    STFMPView(connection,request);
                }else if(request.getAction().equals(STFMPActions.CLOSE)){
                    STFMPClose(connection,scanner,inputStream);
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void sendResponse(Socket connection,STFMPResponse response) throws IOException {
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        String rawResponse = response.rawResponse();
        String encryptedResponse = Constants.ENCRYPT(rawResponse, KEY);
        System.out.println("Responding: "+encryptedResponse);
        printWriter.write(encryptedResponse);
        printWriter.flush();
    }

    private static void STFMPWrite(Socket connection, STFMPRequest request) throws IOException{

        STFMPResponse response;
        String params = request.getParams();
        if (params.split("#").length != 2) {
            response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.INVALID,STFMPMessage.INVALID);
        }else{
            System.out.println("Writing...");
            String filename = request.getFilename();
            String content = request.getContent();
            STFMPActions.writeFile(filename, content);
            response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.OK,STFMPMessage.SUCCESS);
        }
        sendResponse(connection,response);
    }

    private static void STFMPView(Socket connection, STFMPRequest request) throws IOException {
        System.out.println("Searching");
        STFMPResponse response;
        String params = request.getParams();

        if (params.split("#").length !=  1 || params.split("#")[0] == null) {
            response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.INVALID,STFMPMessage.INVALID);
        }else{
            String filename = request.getFilename();
            filename = filename.trim();
            if(STFMPActions.searchFile(filename) == 200){
                String content = STFMPActions.readFile(filename);
                response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.OK,content);
            }else{
                response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.NOT_FOUND,STFMPMessage.NOT_FOUND);
            }


        }
        sendResponse(connection,response);
    }

    private static void STFMPClose(Socket connection, Scanner scanner,InputStream inputStream) throws IOException {
        System.out.println("Connection is closed");
        STFMPResponse response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.OK,STFMPMessage.CLOSE);
        sendResponse(connection,response);
        scanner.close();
        inputStream.close();
        connection.close();
    }

    private static int randomKey(){
        int key;
        Random random = new Random();
        key = random.nextInt(9)+1;
        return key;
    }

    private static void sendKey(Socket connection) throws IOException {
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        KEY = randomKey();
        printWriter.write(KEY+"\r\n");
        System.out.println("Sending Key:" + KEY);
        printWriter.flush();
    }

}
