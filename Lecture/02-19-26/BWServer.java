import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.concurrent.*;

public class BWServer {
    public static final void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(500);
        
        int port = 80;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                pool.execute(new HTTPHandler(serverSocket.accept()));
            }
            
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}

class HTTPHandler implements Runnable {
    private Socket socket;
    public HTTPHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String requestLine = null;
            String referer = null;
            while ((requestLine = in.readLine()) != null && in.readLine().length() > 0) {
                System.out.println("Received message from client: " + requestLine);
                if (requestLine.startsWith("Referer: ")) {
                    referer = requestLine.substring(16); // Remove "Referer: http://" prefix
                }
            }
            //read what file they asked for and respond appropriately
            String[] requestParts = referer.split("/");
            String fileName = "BWServer.java";
            if (referer != null) {
                System.out.println("Client requested: " + requestParts[1]);
                fileName = requestParts[1];
            }
            PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/java");
            /* String html = "<html><body><h1>I'm a teapot</h1><p>bye world</p></body></html>";
            byte[] htmlBytes = html.getBytes(); */

            byte[] htmlBytes = Files.readAllBytes(new File(fileName).toPath());
            out.println("Content-Length: " + htmlBytes.length);
            out.println(); // Empty line to end headers
            out.write(htmlBytes);
            out.flush();
            System.out.println("Sent response to client");
            out.close();

            socket.close();
        } catch (IOException ioe) {
            System.err.println("Error handling client connection: " + ioe.getMessage());
        }
    }
}