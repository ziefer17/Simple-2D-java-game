/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Net;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private Map<Integer, PlayerData> players = new HashMap<>(); // Player ID -> PlayerData
    private int nextPlayerId = 1;

    public GameServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket, nextPlayerId++);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Broadcast player positions to all clients
    private synchronized void broadcastPlayerPositions() {
        StringBuilder message = new StringBuilder("POSITIONS:");
        for (PlayerData player : players.values()) {
            message.append(player.id).append(",")
                   .append(player.x).append(",")
                   .append(player.y).append(";");
        }
        for (ClientHandler client : clients) {
            client.sendMessage(message.toString());
        }
    }

    // Update player position
    private synchronized void updatePlayerPosition(int id, float x, float y) {
        players.put(id, new PlayerData(id, x, y));
        broadcastPlayerPositions();
    }

    // Remove player on disconnect
    private synchronized void removePlayer(int id) {
        players.remove(id);
        clients.removeIf(client -> client.getPlayerId() == id);
        broadcastPlayerPositions();
    }

    // Inner class to handle client connections
    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private int playerId;

        public ClientHandler(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int getPlayerId() {
            return playerId;
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try {
                // Send initial player ID to client
                out.println("ID:" + playerId);
                // Add player to the game with default position
                updatePlayerPosition(playerId, 100, 100); // Default spawn position

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("MOVE:")) {
                        String[] parts = inputLine.substring(5).split(",");
                        float x = Float.parseFloat(parts[0]);
                        float y = Float.parseFloat(parts[1]);
                        updatePlayerPosition(playerId, x, y);
                    }
                }
            } catch (IOException e) {
                System.out.println("Client " + playerId + " disconnected");
            } finally {
                removePlayer(playerId);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Simple data class to store player info
    private static class PlayerData {
        int id;
        float x, y;

        PlayerData(int id, float x, float y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        GameServer server = new GameServer(12345); // Choose a port
        server.start();
    }
}
