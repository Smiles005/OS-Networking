import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import csci1140.*;

public class ChatClient {

    public static final void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java ChatClient server_address");
            return;
        }

        Socket socket = null;
        ExecutorService executioner = Executors.newFixedThreadPool(2);

        try {
            InetAddress server = InetAddress.getByName(args[0]);
            socket = new Socket(server, ChatServer.PORT);

            executioner.submit(new Handler(socket));
            executioner.submit(new KeyBoardAndOutput(socket));

        } catch (UnknownHostException uhe) {
            System.out.println("Sorry, I couldn't find the server " + args[0]);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }

    /**
     * Handles keyboard input and sends data to the server
     */
    private static class KeyBoardAndOutput implements Runnable {
        private Socket socket;

        KeyBoardAndOutput(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            KeyboardReader keyIn = new KeyboardReader();
            PrintWriter out = null;

            try {
                out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                        true);

                // 1️⃣ Send username
                String username = keyIn.readLine("Enter your username: ");
                out.println(username);

                // 2️⃣ Send messages (recipient + message)
                while (true) {
                    String toUser = keyIn.readLine("Send to user: ");
                    String message = keyIn.readLine("Message: ");

                    out.println(toUser);
                    out.println(message);
                }

            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            } finally {
                try { socket.close(); } catch (Exception e) {}
            }
        }
    }

    /**
     * Handles input from the server and prints it
     */
    private static class Handler implements Runnable {
        private Socket socket;

        Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            BufferedReader in = null;

            try {
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                // Print everything the server sends
                String input;
                while ((input = in.readLine()) != null) {
                    System.out.println(input);
                }

            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            } finally {
                try { socket.close(); } catch (Exception e) {}
            }
        }
    }
}
