
package Network;

import Packets.MovementPacket;
import Packets.RemovePlayerPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable {

    private  String host;
    private int port;
    
    private  Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;
    private EventListener listener;
    
    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
    }
    
    public void connect()
    {
        try {
            socket = new Socket(host,port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            listener = new EventListener();
            new Thread(this).start();
        } catch(ConnectException e)
        {
            System.out.println("unable to connect to server");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            running = false;     
            RemovePlayerPacket packet = new RemovePlayerPacket();
            sendObject(packet);
            in.close();
            out.close();
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    //send data to server
    public void sendObject(Object packet)
    {
        try{
            out.writeObject(packet);
            out.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void run() {
        try {
            running = true;
            
            while (running) {
                try {
                    Object data = in.readObject();
                    
                    listener.received(data);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    close();
                }
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // Client 1
        Client client1 = new Client("localhost", 12345);
        client1.connect();
        System.out.println("Client 1 connected");

        // Client 2
        Client client2 = new Client("localhost", 12345);
        client2.connect();
        System.out.println("Client 2 connected");

        // Simulate player actions (e.g., sending movement packets)
        // For testing, send a movement packet from Client 1
        try {
            Thread.sleep(1000); // Wait for connections to establish
            client1.sendObject(new MovementPacket(1, 10.0f, 20.0f));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
