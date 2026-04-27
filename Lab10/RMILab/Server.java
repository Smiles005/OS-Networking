import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import csci1140.KeyboardReader;
        
public class Server implements Hello {
        
    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }

    public String sendMessage(String message) {
        System.out.println("Message received: " + message);
        KeyboardReader keyin = new KeyboardReader();
        String response = keyin.readLine("Enter your response: ");
        return "Message received: " + message + " \n response: " + response;
    }
    
        
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            System.out.println(registry);
            registry.rebind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
// java -Djava.rmi.server.codebase=file:. Server
// start rmiregistry or rmiregistry