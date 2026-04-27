// import java.rmi.registry.*;
// import csci1140.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import csci1140.KeyboardReader;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            System.out.println(registry);
            Hello stub = (Hello) registry.lookup("Hello");
            KeyboardReader keyin = new KeyboardReader();


            String myName = keyin.readLine("Enter your name: ");

            // Partner you SEND to
            // String sendName = keyin.readLine("Enter the name of the person you send TO: ");
            // String sendIP = keyin.readLine("Enter their IP address: ");
            String message = keyin.readLine("Enter the message you want to send: ");

            /* String response = stub.sayHello();
            System.out.println("Response received: " + response); */
            String response = stub.sendMessage(message);
            System.out.println("Response received: " + response);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
//modify the code so that you have a simple message-passing application.  The client should read a message from the keyboard and pass it to the server.  The server will then print the message, read a response from its keyboard, and return it to the client, which then displays the response.