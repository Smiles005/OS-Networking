import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.*;
public class Define {
    public static void main(String[] args) {
        Socket dictionarySocket = null;
        try{
            dictionarySocket = new Socket("dict.org", 2628);
            PrintStream out = new PrintStream(new BufferedOutputStream(dictionarySocket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(dictionarySocket.getInputStream()));
            out.println("SHOW DB");
            out.flush();
            String input=null;
            while((input=in.readLine())!="."){
                System.out.println(input);
            }

            out.println("DEFINE eng-lat computer");
            out.flush();
            while((input=in.readLine())!="."){
                System.out.println(input);
            }
            //devil
            out.println("DEFINE fd-lat-eng deus");
            out.flush();
            while((input=in.readLine())!=null){
                System.out.println(input);
            }






        }catch(IOException e){
            e.printStackTrace(System.err);
        }finally{
            try {
                dictionarySocket.close();
            } catch (Exception e) {
            }
        }
    }
}
