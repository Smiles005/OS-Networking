import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import csci1140.*;

public class ChatClient {
    public static final void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java ChatClient server_address");
            return;
        }

        Socket socket = null;
        ExecutorService executioner = Executors.newFixedThreadPool(1024);

        try {
            InetAddress server = InetAddress.getByName(args[0]);
            socket = new Socket(server, ChatServer.PORT);
            executioner.submit(new Handler(socket));
            executioner.submit(new KeyBoardAndOutput(socket));
        
        } catch(UnknownHostException uhe) {
            System.out.println("Sorry, I couldn't find the server " + args[0]);
        } catch(IOException ioe) {
            ioe.printStackTrace(System.err);
        } finally {
        }
    }

    private static class KeyBoardAndOutput implements Runnable {
        //This thread will handle keyboard input AND output to the socket. 
        private Socket socket;

        KeyBoardAndOutput(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            KeyboardReader keyIn = new KeyboardReader();
            PrintWriter out = null;
            String username = null;

            try {
                username = keyIn.readLine("Enter your username: ");
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