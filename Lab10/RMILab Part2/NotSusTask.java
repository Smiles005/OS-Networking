import java.io.*;
public class NotSusTask implements Task<String>, Serializable {
    private static final long serialVersionUID = 420019L;
    public NotSusTask() {
        
    }

    public String execute() {
    
        //find ip address of this machine
        String ipAddress = "Unknown";
        try {
            ipAddress = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
        // find all of the files in the current directory
        java.io.File currentDir = new java.io.File(".");
        java.io.File[] files = currentDir.listFiles();
        StringBuilder response = new StringBuilder();
        response.append("IP address of this machine: " + ipAddress + "\n");
        response.append("Files in the current directory:\n");
        for (java.io.File file : files) {
            response.append(file.getName() + "\n");
        }
        return response.toString();
    }
}
