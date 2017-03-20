import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.Date;

public class MultiThreadHTTPServer {
    public static void main(String[] args) throws IOException {
        int portNumber = 8080;
        if (args.length >= 1) {
            portNumber = Integer.parseInt(args[0]);
            System.out.println("Using provided port: "+portNumber);
        }        
        ServerSocket serverSocket =
                new ServerSocket(portNumber);
        System.out.println("MyHTTPServer is running at port " + 
                                           serverSocket.getLocalPort());
        while(true){
            Socket clientSocket = serverSocket.accept(); 
            System.out.println("A client is connected from IP: " + 
                        clientSocket.getInetAddress().getHostAddress());    
            new HTTPServerThread(clientSocket).start();
            
        }
    }
}

class HTTPServerThread  extends Thread {
    private Socket clientSocket = null;
 
    HTTPServerThread(Socket clientSocket) {
        super("MyHTTPServerThread");
        this.clientSocket = clientSocket;
    }
     
    public void run() {
 
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
    
            String inputLine=in.readLine();
            while (!inputLine.isEmpty()) {
                System.out.println("received from client: " + inputLine);
                inputLine=in.readLine();
            }
            
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
            httpResponse+="MyHTTPServer\n";
            httpResponse+="Time:" + (new Date()).toString()+"\n";
            clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            System.out.println("Sent to client:"+httpResponse);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class HTTPServerRunnable implements Runnable {
   private Thread t;
   private Socket clientSocket = null;

   HTTPServerRunnable( Socket clientSocket) {
      this.clientSocket = clientSocket;
   }
   
   public void run() {
      //code to handle
   }
   
   public void start () {
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
}

