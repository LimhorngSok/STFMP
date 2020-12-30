import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class STFMPSever {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Bind to port 9999");

            while (true){
                System.out.println("Waiting to accept a client");
                Socket connection = serverSocket.accept();

                System.out.println("Read request from the clients");
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()){
                    String request = new scanner.nextLine();
                    if(request.isEmpty()){
                        break;
                    }
                    System.out.println("Request:" + request);
                }

                System.out.println("Send response to client");
                OutputStream outputStream = connection.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                String respsonseBody = "Hello client. Welcome to Server";
                Map<String, String> responseHeaders = new HashMap<String, String>();
                responseHeaders.put("cont")
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
