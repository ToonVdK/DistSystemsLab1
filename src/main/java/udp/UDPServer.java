package udp;

import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        int port = 1234;
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server listening on port " + port);

            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            // Wait for client request
            socket.receive(receivePacket);
            String fileName = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            System.out.println("Client requested: " + fileName);

            // Get client info to send data back
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            File file = new File("server_files/" + fileName);
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] sendBuffer = new byte[1024];
                int bytesRead;

                // Send file in chunks (datagrams)
                while ((bytesRead = fileInputStream.read(sendBuffer)) != -1) {
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, bytesRead, clientAddress, clientPort);
                    socket.send(sendPacket);
                }

                // Send an empty packet to signify end of file
                DatagramPacket endPacket = new DatagramPacket(new byte[0], 0, clientAddress, clientPort);
                socket.send(endPacket);
                fileInputStream.close();
                System.out.println("File sent over UDP.");
            }
        } catch (IOException e) {
            System.out.println("UDP Server Error: " + e);
        }
    }
}
