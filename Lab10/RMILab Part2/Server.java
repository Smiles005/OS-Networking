import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import csci1140.KeyboardReader;
        
public class Server implements RemoteTask {
        
    public Server() {}

    public String sayRemoteTask() {
        return "RemoteTask, world!";
    }

    public <T> T executeTask(Task<T> t) {
        return t.execute();
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
            RemoteTask stub = (RemoteTask) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            System.out.println(registry);
            registry.rebind("RemoteTask", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
// java -Djava.rmi.server.codebase=file:. Server
// start rmiregistry or rmiregistry
/*
Modify your server so that it implements RemoteTask.  Compile and start the server.

Come up with two more "tasks" for the server to execute. 
Write those classes and verify the server can execute them without recompiling the server.
*/