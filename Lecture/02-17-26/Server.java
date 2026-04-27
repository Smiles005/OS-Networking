import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(30);
        
        int port = 12345;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                pool.execute(new ConnectionHandler(serverSocket.accept()));
                System.out.println("Client connected: " + socket.getInetAddress());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Received message from client: " + in.readLine());
            
            }
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}
class ConnectionHandler implements Runnable {
    private Socket socket;
    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (int i = 0; i < 5; ) {
                if (in.ready()) {
                    System.out.println("Received message from client: " + in.readLine());
                    i++;
                }

            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Error handling client connection: " + e.getMessage());
        }
    }
}