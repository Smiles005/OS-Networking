import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.Buffer;


public class UDPTest {
    private static final int PORT = 12345;
    private static DatagramSocket socket;
    static {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException se) {
            se.printStackTrace(System.err);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("UDP Server is listening on port 12345...");
            String data = "Hello, UDP Client!";
            byte[] dataBytes = data.getBytes();
            System.arraycopy(dataBytes, 0, buffer, 0, dataBytes.length);
            packet.setPort(PORT);
            packet.setAddress(InetAddress.getLocalHost());
            socket.send(packet);
            Arrays.fill(buffer, (byte)0); // Clear the buffer for receiving
            socket.receive(packet);
            System.out.println(new String(buffer));
        }catch (SocketException se) {
            se.printStackTrace(System.err);
        }catch (UnknownHostException uhe) {
            uhe.printStackTrace(System.err);

        }catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }

        

        boolean running = true;
        
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received message: " + message);
            // Echo the message back to the sender
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            DatagramPacket responsePacket = new DatagramPacket(message.getBytes(), message.length(), clientAddress,
                    clientPort);
            socket.send(responsePacket);
        

    }
}
