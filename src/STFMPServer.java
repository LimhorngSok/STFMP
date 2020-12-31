import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class STFMPServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(9999)) {

            System.out.println("Bind to port 9999");

            while (true){
                System.out.println("Waiting to accept a client");
                Socket connection = serverSocket.accept();

                System.out.println("Read request from the clients");
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                //Get Encryption
                String encryptedRequest = scanner.nextLine();
                System.out.println("Receiving:"+encryptedRequest);
                //Decryption
                String rawRequest = Constants.DECRYPTE(encryptedRequest);
                System.out.println("Decrypting:"+rawRequest);
                //Creating Request
                STFMPRequest request = STFMPRequest.fromRawString(rawRequest);
                System.out.println("Send response to client");

                if(request.getAction().equals(STFMPActions.WRITE)){
                    STFMPWrite(connection,request);
                }else if(request.getAction().equals(STFMPActions.VIEW)){
                    STFMPView(connection,request);
                }else if(request.getAction().equals(STFMPActions.CLOSE)){
                    STFMPClose(connection,scanner,inputStream);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void sendResponse(Socket connection,STFMPResponse response) throws IOException {
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        String encryptedResponse = Constants.ENCRYPTE(response.toString());
        System.out.println("Encrypting:"+encryptedResponse);
        printWriter.write(encryptedResponse);
        printWriter.flush();
    }

    private static void STFMPWrite(Socket connection, STFMPRequest request) throws IOException{
        System.out.println("Writing");
        STFMPResponse response;
        String filename = request.getFilename();
        String content = request.getContent();
        if (filename == null || content == null) {
            response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.INVALID,STFMPMessage.INVALID);
        }else{
            STFMPActions.writeFile(filename, content);
            response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.OK,STFMPMessage.SUCCESS);
        }
        sendResponse(connection,response);
    }

    private static void STFMPView(Socket connection, STFMPRequest request) throws IOException {
        System.out.println("Searching");
        STFMPResponse response;
        String filename = request.getFilename();
        if (filename == null ) {
            response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.INVALID,STFMPMessage.INVALID);
        }else{
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
        STFMPResponse response = new STFMPResponse(Constants.PROTOCOL_VERSION,STFMPStatus.NOT_FOUND,STFMPMessage.NOT_FOUND);
        sendResponse(connection,response);
        scanner.close();
        inputStream.close();
        connection.close();
    }

}