import java.rmi.*;

public interface RemoteTask extends Remote {
    final String NAME = "RemoteTask";
    <T> T executeTask(Task<T> t) throws RemoteException;

    //Frog: this is my leave it alone frog
}