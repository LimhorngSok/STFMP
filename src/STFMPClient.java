import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class STFMPClient {
    public static void main(String[] args) {
        try (Socket connection = new Socket("localhost", 9999);){

            System.out.println("The connection to the server was established.");

            // Send request to the server
            System.out.println("Send data to the server");

            STFMPRequest request = new STFMPRequest(Constants.PROTOCOL_VERSION,STFMPActions.CLOSE,"2.txt");
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            //Encryption
            String encryptedRequest = request.encryptedRequest();
            printWriter.write(encryptedRequest);
            printWriter.flush();
            System.out.println("Requesting:" + encryptedRequest);

            // Read data from the server
            InputStream inputStream = connection.getInputStream();
            System.out.println("Receiving from server...");
            Scanner scanner = new Scanner(inputStream);
            String encryptedResponse = scanner.nextLine();
            System.out.println("Receiving:" + encryptedResponse);
            //Decryption
            STFMPResponse response = STFMPResponse.fromEncryptedString(encryptedResponse);
            System.out.println("The result is: " + response.getMessage());

            scanner.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Cannot connect to the server. " + e.getMessage());
        }
    }
}
