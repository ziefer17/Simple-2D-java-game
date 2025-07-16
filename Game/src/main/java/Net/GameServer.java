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
    private Map<Integer, PlayerData> players = new HashMap<>();
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
                System.out.println("New client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                ClientHandler clientHandler = new ClientHandler(clientSocket, nextPlayerId++);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                // Broadcast current state to ensure new client sees existing players
                broadcastPlayerPositions();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void broadcastPlayerPositions() {
        StringBuilder message = new StringBuilder("POSITIONS:");
        for (PlayerData player : players.values()) {
            message.append(player.id).append(",")
                   .append(player.x).append(",")
                   .append(player.y).append(";");
        }
        String msg = message.toString();
        System.out.println("Broadcasting to " + clients.size() + " clients: " + msg);
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }

    private synchronized void updatePlayerPosition(int id, float x, float y) {
        players.put(id, new PlayerData(id, x, y));
        broadcastPlayerPositions();
    }

    private synchronized void removePlayer(int id) {
        players.remove(id);
        clients.removeIf(client -> client.getPlayerId() == id);
        System.out.println("Player " + id + " disconnected, broadcasting updated positions");
        broadcastPlayerPositions();
    }

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
                updatePlayerPosition(playerId, 100, 100);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("MOVE:")) {
                        try {
                            String[] parts = inputLine.substring(5).split(",");
                            float x = Float.parseFloat(parts[0]);
                            float y = Float.parseFloat(parts[1]);
                            updatePlayerPosition(playerId, x, y);
                        } catch (Exception e) {
                            System.out.println("Error parsing MOVE from player " + playerId + ": " + inputLine);
                            e.printStackTrace();
                        }
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
        GameServer server = new GameServer(12345);
        server.start();
    }
}
