
package Network;

import Packets.MovementPacket;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("192.168.1.4", 12345);
        client.connect();
        System.out.println("Client connected");

        // Simulate movement after a delay
        try {
            Thread.sleep(1000);
            client.sendObject(new MovementPacket(0, 10.0f, 20.0f)); // ID will be set by server
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
