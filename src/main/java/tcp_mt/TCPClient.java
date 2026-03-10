package tcp_mt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Loopback address
        int port = 5000;
        String requestedFile = "test.txt";

        try (Socket socket = new Socket(serverAddress, port)) {
            // Setup output (to ask for file) and input (to receive file)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStream in = socket.getInputStream();

            // Send filename to server
            out.println(requestedFile);

            // Setup to save the file locally
            FileOutputStream fileOutputStream = new FileOutputStream("client_files/downloaded_" + requestedFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read from network socket and write to local file
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            System.err.println("Could not connect to " + serverAddress);
        }
    }
}
