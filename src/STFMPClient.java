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

            STFMPRequest request = new STFMPRequest(Constants.PROTOCOL_VERSION,STFMPActions.CLOSE,"result_1.txt","Selected Topic Network");
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            //Encryption
            String encryptedRequest = Constants.ENCRYPTE(request.toString());
            printWriter.write(encryptedRequest);
            printWriter.flush();
            System.out.println("Requesting:" + encryptedRequest);

            // Read data from the server
            InputStream inputStream = connection.getInputStream();
            System.out.println("Receiving from server...");
            Scanner scanner = new Scanner(inputStream);
            System.out.println("Receiving from server...");
            String encryptedResponse = scanner.nextLine();
            //Decryption
            String rawResponse = Constants.DECRYPTE(encryptedResponse);
            STFMPResponse response = STFMPResponse.fromRawString(rawResponse);
            System.out.println("The result is: " + response.getMessage());

            scanner.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Cannot connect to the server. " + e.getMessage());
        }
    }
}
