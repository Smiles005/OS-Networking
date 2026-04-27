import java.io.IOException;
import java.net.InetAddress;
import csci1140.*;

public class CheckIP {
    public static void main(String[] args) {
        KeyboardReader k = new KeyboardReader();
        if (args.length < 1) {
            System.out.println("Usage: java CheckIP <IP_ADDRESS>");
            checkIP(k.readLine("Please enter an IP address to check: "));
        } else {
            System.out.println("Checking IP address: " + args[0]);
            checkIP(args[0]);
        }
    }

    public static void checkIP(String ip) {
        try {

            InetAddress inetAddress = InetAddress.getByName(ip);
            if (inetAddress.isReachable(100)) { // Check if the host is reachable
                System.out.println("Reachable Host: " + inetAddress.getHostName() + " - IP: " + ip);
            }else{
                
            }
        } catch (IOException e) {
        }
    }
}
