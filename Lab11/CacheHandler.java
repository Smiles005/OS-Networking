import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.*;

public interface CacheHandler extends Remote {
    String getURL(URL url) throws RemoteException;
}