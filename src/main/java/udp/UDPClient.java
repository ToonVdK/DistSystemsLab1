package udp;

import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 1234;
        String requestedFile = "test.txt";

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(serverAddress);

            // Send request
            byte[] requestBytes = requestedFile.getBytes();
            DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length, address, port);
            socket.send(requestPacket);

            // Receive file
            FileOutputStream fileOutputStream = new FileOutputStream("client_files/udp_downloaded_" + requestedFile);
            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                // If we receive an empty packet, it's the end of the file
                if (receivePacket.getLength() == 0) {
                    break;
                }
                fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
            }

            fileOutputStream.close();
            System.out.println("File downloaded via UDP.");
        } catch (IOException e) {
            System.out.println("Client Error: " + e);
        }
    }
}
