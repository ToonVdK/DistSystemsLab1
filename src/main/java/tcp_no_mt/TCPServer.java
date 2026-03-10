package tcp_no_mt;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            // Wait for a client to connect
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            // Setup input (to read filename) and output (to send file)
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            String fileName = in.readLine(); // Read the request of the client
            System.out.println("Client requested file: " + fileName);

            // Look for the requested file
            File file = new File("server_files/" + fileName);
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;

                // Read from file and write to network socket
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
                System.out.println("File sent successfully.");
            } else {
                System.out.println("File not found.");
            }
            socket.close();

        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
        }
    }
}
