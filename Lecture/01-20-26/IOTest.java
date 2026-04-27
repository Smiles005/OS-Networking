import java.io.*;
public class IOTest {
    public static void main(String[] args) {
        PrintStream out = null;
        try{
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream("coolfile.txt")));
            out.println("Hello, World!");
            // out.flush();
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace(System.err);
        }finally{
            try {
                out.close();
            } catch (Exception e) {}
            System.out.println("Finished writing to file.");
        }
        // Reading the file back
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(new FileInputStream("coolfile.txt")));
            String line;
            while((line = in.readLine()) != null){
                System.out.println("Read from file: " + line);
            }
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace(System.err);
        }catch(IOException ioe){
            ioe.printStackTrace(System.err);

        }finally{
            try {
                in.close();
            } catch (Exception e) {}
        }

        // Demonstrating try-with-resources
        try(BufferedReader in2 = new BufferedReader(new InputStreamReader(new FileInputStream("coolfile.txt")))){
            String line2 = null;
            while((line2 = in2.readLine()) != null){
                System.out.println("Read again from file: " + line2);
            }
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace(System.err);
        }catch(IOException ioe){
            ioe.printStackTrace(System.err);
        }
    }
}