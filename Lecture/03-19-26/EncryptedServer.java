
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocketFactory;


public class EncryptedServer {
    public static void main(String[] args) {
        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            String[] defaultSuites = factory.getDefaultCipherSuites();
            System.out.println("Default Cipher Suites:");
            for (String suite : defaultSuites) {
                System.out.println(" \t " + suite);
            }
            String[] supportedSuites = factory.getSupportedCipherSuites();
            System.out.println("Supported Cipher Suites:");
            for (String suite : supportedSuites) {
                System.out.println(" \t " + suite);
            }
            ServerSocket serverSocket = factory.createServerSocket(443);
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = null;
            while ((input = reader.readLine()) != null) {
                System.out.println(input);

            }
        socket.close();
        serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
