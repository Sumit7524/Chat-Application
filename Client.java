import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
// import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
// import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
// import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// import java.io.*;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea MessageArea = new JTextArea();
    private JTextField MessageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor
    public Client() {
        try {
            System.out.println("Sending Request to server");
            socket = new Socket("127.0.0.1", 7778);
            System.out.println("connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            hangleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
        }
    }

    private void hangleEvents() {
        MessageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // throw new UnsupportedOperationException("Unimplemented method
                // 'keyReleased'");
                System.out.println("Key Released" + e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("you have pressed enter button");
                    String contentToSend=MessageInput.getText();
                    MessageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    MessageInput.setText("");
                    MessageInput.requestFocus();


                }
            }

        });

    }

    private void createGUI() {
        // GUI Create
        this.setTitle("Client Messanger[END]");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Coding for Component
        heading.setFont(font);
        MessageArea.setFont(font);
        MessageArea.setFont(font);

        // =================== img logo======================
        // heading.setIcon(new ImageIcon("logo.png" ));
        ImageIcon icon = new ImageIcon("logo.png");
        Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        heading.setIcon(new ImageIcon(image));

        MessageInput.setHorizontalAlignment(SwingConstants.CENTER);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        MessageArea.setEditable(false);

        // Frame ka layout set krenge
        this.setLayout(new BorderLayout());
        // Adding components in frame

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(MessageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(MessageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    public void startReading() {
        // Thread - read karke deta rahega
        Runnable r1 = () -> {
            System.out.println("Reader Started");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server  Terminated Chat");
                        JOptionPane.showMessageDialog(this," Server Terminated the chat");
                        MessageInput.setEnabled(false);
                        socket.close();
                        break;

                    }
                    // System.out.println("Server : " + msg);
                    MessageArea.append("Server :" + msg+"\n");

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is Closed");

            }

        };
        new Thread(r1).start();

    }

    public void startWriting() {
        // Thread - data user lega and the send karega client tak
        Runnable r2 = () -> {
            System.out.println("Writer Started...");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;

                    }

                }
                // System.out.println("Connection is Closed");

            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is Client");
        new Client();
    }

}
