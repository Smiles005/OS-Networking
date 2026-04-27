import csci1140.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChatServer {
    public static final int PORT = 2048;
    public static final void main(String[] args) {
        ExecutorService executioner = Executors.newFixedThreadPool(1024);
        
        try (ServerSocket ss = new ServerSocket(PORT)) {
            boolean StayingAlive = true;
            while (StayingAlive) { 
                Socket client = ss.accept();
                System.out.println("ChatServer is starting on port " + PORT);
                executioner.submit(new KeyBoardAndOutput(client)); // it is getting stuck in the thread, if this one is first, then the server can only output, but not accept any clients. 
                executioner.submit(new Handler(client)); // If the handler is first, then the server can only accept clients, but not output to the screen. So we need to start both threads at the same time, and they will both be able to run concurrently. I thought that is what the ExecutorService is for, but it seems that it is not working as expected.
                KeyboardReader keyIn = new KeyboardReader();
                String input = keyIn.readLine("Type 'exit' to stop the server: ");
                if (input.equalsIgnoreCase("exit")) {
                    StayingAlive = false;
                    executioner.shutdownNow();
                    System.out.println("ChatServer is shutting down.");
                }

            }
            
        } catch(IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }

    private static class KeyBoardAndOutput implements Runnable {
        //This thread will handle keyboard input AND output to the socket. 
        private Socket socket;

        KeyBoardAndOutput(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            System.out.println("ChatServer: Opening output to the socket");
            KeyboardReader keyIn = new KeyboardReader();
            PrintWriter out = null;
            String username = null;

            try {
                username = keyIn.readLine("Please enter your username: ");
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                out.println(username);
                out.flush();

                while(true) {
                    String input = keyIn.readLine("What do you want to say: ");
                    out.println(input);
                    out.flush();
                }
            } catch(IOException ioe) {
                ioe.printStackTrace(System.err);
            } finally {
                try {
                    out.close();
                } catch(Exception e) {}
                try {
                    socket.close();
                } catch(Exception e){}
            }
        }
    }

    private static class Handler implements Runnable {
        //This thread will handle input from the socket AND writing to the screen.
        private Socket socket;

        Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            System.out.println("ChatServer: Opening input from the socket");
            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
                String username = null;
                while (username == null) {
                    username = in.readLine();
                }

                while(true) {
                    String input = null;
                    while((input = in.readLine()) != null) {
                        System.out.println(username + " says: " + input);
                    }
                }
            } catch(IOException ioe) {
                ioe.printStackTrace(System.err);
            } finally {
                try {
                    in.close();
                } catch(Exception e){}
                try {
                    socket.close();
                } catch(Exception e){}
            }
        }
    }
}
