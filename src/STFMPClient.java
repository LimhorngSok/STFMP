import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class STFMPClient {
    private static int KEY = 0;
    private static boolean CONNECTION;
    public static void main(String[] args) {
        try (Socket connection = new Socket("localhost", 9999);){
            CONNECTION = true;
            System.out.println("The connection to the server was established.");
            if(KEY == 0){
                getKey(connection);
            }
            while (CONNECTION == true){
                System.out.println("Choose one of the options below:");
                System.out.println("1. Write file");
                System.out.println("2. View file");
                System.out.println("3. Close connection");

                System.out.println("Your input:");
                Scanner userInput = new Scanner(System.in);
                int option = userInput.nextInt();
                //Get Key

                switch(option){
                    case 1:
                        writeFile(connection);
                        break;
                    case 2:
                        viewFile(connection);
                        break;
                    case 3:
                        closeConnection(connection);
                        CONNECTION = false;
                        break;
                }
            }


        } catch (IOException e) {
            System.out.println("Cannot connect to the server. " + e.getMessage());
        }
    }

    private static void getKey(Socket connection) throws IOException {
        InputStream getInputKey = connection.getInputStream();
        System.out.println("Receiving a key from server...");
        Scanner getKey = new Scanner(getInputKey);
        KEY = Integer.parseInt(getKey.nextLine());
        System.out.println("Received Key: " + KEY);


    }
    private static void writeFile(Socket connection) throws IOException {
        //GET FILE NAME AND CONTENT
        System.out.println("Input filename:");
        Scanner filenameInput = new Scanner(System.in);
        String filename = filenameInput.nextLine();
        System.out.println("Input content:");
        Scanner contentInput = new Scanner(System.in);
        String content = contentInput.nextLine();
        String params = filename+"#"+content;

        STFMPRequest request = new STFMPRequest(Constants.PROTOCOL_VERSION,STFMPActions.WRITE,params);
        // Send request to the server
        System.out.println("Send data to the server");
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        //Encryption
        String rawRequest = request.rawRequest();
        String encryptedRequest = Constants.ENCRYPT(rawRequest,KEY);
        printWriter.write(encryptedRequest);
        printWriter.flush();
        System.out.println("Requesting:" + encryptedRequest);

        getResponse(connection);
    }

    private static void viewFile(Socket connection) throws IOException {
        //GET FILE NAME
        System.out.println("Input filename:");
        Scanner filenameInput = new Scanner(System.in);
        String filename = filenameInput.nextLine();
        String params = filename;

        STFMPRequest request = new STFMPRequest(Constants.PROTOCOL_VERSION,STFMPActions.VIEW,params);
        // Send request to the server
        System.out.println("Send data to the server");
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        //Encryption
        String rawRequest = request.rawRequest();
        String encryptedRequest = Constants.ENCRYPT(rawRequest,KEY);
        printWriter.write(encryptedRequest);
        printWriter.flush();
        System.out.println("Requesting:" + encryptedRequest);

        getResponse(connection);
    }

    private static void closeConnection(Socket connection) throws IOException {
        // Send request to the server
        System.out.println("Send data to the server");

        STFMPRequest request = new STFMPRequest(Constants.PROTOCOL_VERSION,STFMPActions.CLOSE,null);
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        //Encryption
        String rawRequest = request.rawRequest();
        String encryptedRequest = Constants.ENCRYPT(rawRequest,KEY);
        printWriter.write(encryptedRequest);
        printWriter.flush();
        System.out.println("Requesting:" + encryptedRequest);

        getResponse(connection);
        connection.close();
    }

    private static void getResponse(Socket connection) throws IOException {
        // Read data from the server
        InputStream inputStream = connection.getInputStream();
        System.out.println("Receiving from server...");
        Scanner scanner = new Scanner(inputStream);
        String encryptedResponse = scanner.nextLine();
        System.out.println("Receiving:" + encryptedResponse);
        //Decryption
        STFMPResponse response = STFMPResponse.decryptResponse(encryptedResponse,KEY);
        System.out.println("The result is: " + response.getMessage());


    }


}
