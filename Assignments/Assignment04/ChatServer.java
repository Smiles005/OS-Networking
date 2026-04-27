import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private static ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Server running on port 4096");

        try (ServerSocket serverSocket = new ServerSocket(4096)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(
                        socket.getOutputStream(), true);

                // Read username
                username = in.readLine();
                clients.put(username, out);
                System.out.println(username + " connected.");

                while (true) {
                    String recipient = in.readLine();
                    String message = in.readLine();
                    if (recipient == null || message == null) break;

                    PrintWriter target = clients.get(recipient);
                    if (target != null) {
                        target.println("From " + username + ": " + message);
                    } else {
                        out.println("User " + recipient + " not connected.");
                    }
                }

            } catch (IOException e) {
                System.out.println(username + " disconnected.");
            } finally {
                if (username != null) clients.remove(username);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}