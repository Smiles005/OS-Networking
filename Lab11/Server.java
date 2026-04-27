// import java.rmi.registry.*;
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.net.URL;
// import java.rmi.*;
// import java.rmi.server.*;

// import csci1140.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import csci1140.KeyboardReader;

public class Server implements CacheHandler {

    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }

    @Override
    public String getURL(URL url) throws RemoteException {

        // Ensure cache directory exists
        File cacheDir = new File("cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        String filename = urlToFilename(url);
        File cacheFile = new File(cacheDir, filename);

        // If NOT in cache, download and save
        if (!cacheFile.exists()) {
            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(cacheFile)) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                e.printStackTrace(System.err);
                return null;
            }
        }

        // File IS in cache → read using BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(cacheFile))) {

            StringBuilder contents = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                contents.append(line).append("\n");
            }

            return contents.toString();

        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public String sendMessage(String message) throws RemoteException {
        System.out.println("Message received: " + message);
        KeyboardReader keyin = new KeyboardReader();
        String response = keyin.readLine("Enter your response: ");
        return "Message received: " + message + "\nResponse: " + response;
    }

    public static void main(String args[]) {

        // Create cache directory if it does not exist
        File cacheDir = new File("cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        // Clear cache on server startup
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        try {
            Server obj = new Server();
            CacheHandler stub = (CacheHandler) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            // Registry registry = LocateRegistry.getRegistry("127.0.0.1", 80);
            registry.rebind("CacheHandler", stub);

            System.out.println("Server ready (Proxy + Cache enabled)");

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    // Check whether a URL is cached
    private boolean isInCache(URL url) {
        File cacheFile = new File("cache", urlToFilename(url));
        return cacheFile.exists();
    }

    // Convert a URL into a filesystem-safe filename
    private String urlToFilename(URL url) {

        String protocol = (url.getProtocol() != null) ? url.getProtocol() : "";
        String host = (url.getHost() != null) ? url.getHost() : "";
        String path = (url.getPath() != null && !url.getPath().isEmpty())
                ? url.getPath()
                : "index";
        String query = (url.getQuery() != null) ? url.getQuery() : "";
        String port = (url.getPort() == -1) ? "" : "_" + url.getPort();

        String raw = protocol + "_" + host + port + "_" + path
                + (query.isEmpty() ? "" : "_" + query);

        // Sanitize filename
        return raw.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
// Modify your RMI server so that it can behave like a basic Proxy Server.

// You will need a cache of already downloaded files, so make a subdirectory for this purpose. You can either make it manually if you want to do it the sad way, or use the File class to create the subdirectory if it does not exist (only for expert programmers and the cool kids).

// Add an interface that contains a method which takes a URL argument and returns a String
// In that method:
// Check to see if the URL argument is in your cache
// If the argument is not in the cache:
// open a connection to the URL, read the contents and save it to a local file
// next, follow the steps for when a file is in the cache
// If the argument is in the cache:
// open the local copy of the file using BufferedReader
// read and return the contents of the file as a String 
// When your server starts, clear the cache (look at the java.io.File class for methods which will help)