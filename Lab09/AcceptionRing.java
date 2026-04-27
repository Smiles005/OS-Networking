// I will need to make use of UDP packets for this lab, which we have discussed, but haven't had a chance to put to good use.  Please read about them in Chapter 12 of your text.

// We are going to simulate a Token Ring Network with UDP packets.  Why? Because using expensive modern computers to simulate 35-year-old technology is all the rage with "the youth" these days :)

// You will each need to partner with two other students.  One student, you will receive packets from, and the other you will send packets to.  Each person must be paired with exactly two other students so that we have a single ring-shaped network of people that includes the entire class. You may not send packets to the person who sends them to you.


// The Token (packet) data format is

// Sender Name: 40 bytes
// Receiver Name: 40 bytes
// Message: 944 bytes


// Your application must prompt the user for the name of the person they would like to send a message to, and for the content of the message.  Once the data is packaged into a packet, send the packet to the partner who receives messages from you.


// Whenever you receive a packet, examine the receiver name.  If the packet is intended for you, print out the name of the sender and the message. 

// If the packet is not intended for you, examine the sender name:

// If the packet is from you, it was never delivered. Print a message indicating delivery failure.
// If the packet is not from you, send it to the partner who receives messages from you.

// Use port number 3072.


// Note that you will need to continually send and receive messages as well as prompting the user for input.  <cough>threads</cough>
import java.net.*;
import java.io.*;
import csci1140.*;

public class AcceptionRing {
    public static final int PORT = 3072;

    public static void main(String[] args) {
        KeyboardReader keyin = new KeyboardReader();

        System.out.println("=== TOKEN RING SIMULATOR ===");

        String myName = keyin.readLine("Enter your name: ");

        // Partner you SEND to
        String sendName = keyin.readLine("Enter the name of the person you send TO: ");
        String sendIP = keyin.readLine("Enter their IP address: ");

        
        Partner sendPartner = null;
        try {
            sendPartner = new Partner(sendName, InetAddress.getByName(sendIP), PORT);
        } catch (Exception e) {
            System.out.println("Invalid IP address.");
            System.exit(1);
        }

        // Start threads
        SenderThread sender = new SenderThread(myName, sendPartner);
        ReceiverThread receiver = new ReceiverThread(myName, sendPartner);

        sender.start();
        receiver.start();

        // Keep main thread alive
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }
    }
}

class Partner {
    private String name;
    private InetAddress ipAddress;
    private int port;

    public Partner(String name, InetAddress ipAddress, int port) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getName() { return name; }
    public InetAddress getIpAddress() { return ipAddress; }
    public int getPort() { return port; }
}

class Token {
    private String senderName;
    private String receiverName;
    private String messageContent;

    public Token(String senderName, String receiverName, String messageContent) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.messageContent = messageContent;
    }

    public Token(byte[] data) {
        this.senderName = new String(data, 0, 40).trim();
        this.receiverName = new String(data, 40, 40).trim();
        this.messageContent = new String(data, 80, 944).trim();
    }

    public String getSenderName() { return senderName; }
    public String getReceiverName() { return receiverName; }
    public String getMessageContent() { return messageContent; }

    public byte[] toByteArray() {
        byte[] data = new byte[1024];
        System.arraycopy(senderName.getBytes(), 0, data, 0, Math.min(40, senderName.length()));
        System.arraycopy(receiverName.getBytes(), 0, data, 40, Math.min(40, receiverName.length()));
        System.arraycopy(messageContent.getBytes(), 0, data, 80, Math.min(944, messageContent.length()));
        return data;
    }
}

class SenderThread extends Thread {
    private String myName;
    private Partner sendPartner;
    private KeyboardReader keyin = new KeyboardReader();

    public SenderThread(String myName, Partner sendPartner) {
        this.myName = myName;
        this.sendPartner = sendPartner;
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            while (true) {
                String receiver = keyin.readLine("Send to: ");
                String message = keyin.readLine("Message: ");

                Token token = new Token(myName, receiver, message);
                byte[] data = token.toByteArray();

                DatagramPacket packet = new DatagramPacket(
                    data,
                    data.length,
                    sendPartner.getIpAddress(),
                    sendPartner.getPort()
                );

                socket.send(packet);
                System.out.println("Packet sent.");
            }
        } catch (Exception e) {
            System.out.println("Sender error: " + e);
        }
    }
}

class ReceiverThread extends Thread {
    private String myName;
    private Partner sendPartner;

    public ReceiverThread(String myName, Partner sendPartner) {
        this.myName = myName;
        this.sendPartner = sendPartner;
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket(AcceptionRing.PORT)) {
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                Token token = new Token(packet.getData());

                // If intended for me
                if (token.getReceiverName().equalsIgnoreCase(myName)) {
                    System.out.println("\n=== MESSAGE RECEIVED ===");
                    System.out.println("From: " + token.getSenderName());
                    System.out.println("Message: " + token.getMessageContent());
                    System.out.println("========================\n");
                }
                else {
                    // If the packet came full circle
                    if (token.getSenderName().equalsIgnoreCase(myName)) {
                        System.out.println("Delivery failed: message returned to sender.");
                    } else {
                        // Forward it
                        DatagramPacket forward = new DatagramPacket(
                            packet.getData(),
                            packet.getLength(),
                            sendPartner.getIpAddress(),
                            sendPartner.getPort()
                        );
                        socket.send(forward);
                        System.out.println("Forwarded packet for " + token.getReceiverName() +" from "+ token.getSenderName()+"\n Data:"+token.getMessageContent() +"\n");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Receiver error: " + e);
        }
    }
}
