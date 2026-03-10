package tcp_mt;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Setup input (to read filename) and output (to send file)
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            String fileName = in.readLine(); // Read the request of the client
            System.out.println("Thread " + Thread.currentThread().getId() + " serving file: " + fileName);

            File file = new File("server_files/" + fileName);
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
