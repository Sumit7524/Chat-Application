import java.io.BufferedReader;
// import jdk.internal.org.jline.utils.InputStreamReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Constructor
    public Server() {
        try {
            server = new ServerSocket(7778);
            System.out.println("server is ready to accept connection");
            System.out.println("Waiting....");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startReading() {
        // Thread - read karke deta rahega
        Runnable r1 = () -> {
            System.out.println("Reader Started");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated The Chat");
                        socket.close();
                        break;

                    }
                    System.out.println("Client : " + msg);

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
                      if (content.equals("exit")) 
                    {
                        socket.close();
                        break;
                        
                    }

                }
            } catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
                System.out.println("Connection is Closed");


            }
            // System.out.println("Connection is Closed");


        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is Server... going to start Server");
        new Server();
    }
}