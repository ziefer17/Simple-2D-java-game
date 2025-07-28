/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import Content.Assets;
import Net.GameServer;
import com.mycompany.game.Game;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GameLauncher extends JFrame{
    private JComboBox<String> textureComboBox;
    private JTextField portField;
    private JTextField ipField;
    private JButton startServerButton;
    private JButton joinGameButton;
    
    public GameLauncher() {
        super("Game Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        
        JLabel textureLabel = new JLabel("Select Texture Set:");
        textureComboBox = new JComboBox<>(new String[]{"Default Textures", "Alternate Textures"});
        
        JLabel portLabel = new JLabel("Enter Server Port:");
        portField = new JTextField("12345");
        
        JLabel ipLabel = new JLabel("Enter Server IP:");
        ipField = new JTextField("192.168.1.5");
        
        startServerButton = new JButton("Start Server and Join");
        joinGameButton = new JButton("Join Game");
        
        add(textureLabel);
        add(textureComboBox);
        add(portLabel);
        add(portField);
        add(ipLabel);
        add(ipField);
        add(startServerButton);
        add(joinGameButton);
        
        startServerButton.addActionListener(e -> startServerAndJoin());
        joinGameButton.addActionListener(e -> joinGame());
    }
    
    private void startServerAndJoin() {
        try {
            int port = Integer.parseInt(portField.getText().trim());
            String ip = ipField.getText().trim();
            boolean useAlternateTextures = textureComboBox.getSelectedIndex() == 1;
            
            new Thread(() -> {
                GameServer server = new GameServer(port);
                server.start();
            }).start();
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            updateTexturePaths(useAlternateTextures);
            
            Game game = new Game("RPG Game", 800, 800, ip);
            game.start();
            
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "enter port", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void joinGame() {
        try {
            String ip = ipField.getText().trim();
            boolean useAlternateTextures = textureComboBox.getSelectedIndex() == 1;
            
            updateTexturePaths(useAlternateTextures);
            
            Game game = new Game("RPG Game", 800, 800, ip);
            game.start();
            
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "server error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTexturePaths(boolean useAlternate) {
        if (useAlternate) {
            Assets.useAlternateTextures = useAlternate;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameLauncher().setVisible(true);
        });
    }
}
