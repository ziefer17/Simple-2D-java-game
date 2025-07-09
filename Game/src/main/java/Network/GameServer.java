
package Network;

import Packets.AddPlayerPacket;
import Packets.MovementPacket;
import Packets.RemovePlayerPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameServer {
    private ServerSocket serverSocket;
    private HashMap<Integer, ClientHandler> clients = new HashMap<>();
    private int nextId = 1;
    private final int maxPlayers = 2;

    public GameServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            acceptClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptClients() {
        try {
            while (clients.size() < maxPlayers) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);
                ClientHandler client = new ClientHandler(socket, nextId);
                clients.put(nextId, client);
                new Thread(client).start();
                // Send AddPlayerPacket to all clients
                AddPlayerPacket packet = new AddPlayerPacket(nextId, "Player" + nextId);
                broadcast(packet);
                nextId++;
            }
            System.out.println("Maximum players reached.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast packet to all clients
    private void broadcast(Object packet) {
        for (ClientHandler client : clients.values()) {
            client.sendObject(packet);
        }
    }

    // Remove a client and notify others
    private void removeClient(int id) {
        clients.remove(id);
        RemovePlayerPacket packet = new RemovePlayerPacket(id);
        broadcast(packet);
    }

    // ClientHandler class to manage each client connection
    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int id;
        private boolean running = true;

        public ClientHandler(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendObject(Object packet) {
            try {
                out.writeObject(packet);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Object packet = in.readObject();
                    // Broadcast received packet to all clients
                    if (packet instanceof MovementPacket || packet instanceof AddPlayerPacket) {
                        broadcast(packet);
                    } else if (packet instanceof RemovePlayerPacket) {
                        running = false;
                        removeClient(id);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    running = false;
                    removeClient(id);
                }
            }
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new GameServer(12345); // Start server on port 12345
    }
}
