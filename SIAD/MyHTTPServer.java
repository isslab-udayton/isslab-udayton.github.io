import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.Date;

public class MyHTTPServer {
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
        Socket clientSocket = serverSocket.accept(); 
        System.out.println("A client is connected from IP: " + 
                        clientSocket.getInetAddress().getHostAddress());    
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
    
        String inputLine=in.readLine();
        while (!inputLine.isEmpty()) {
                System.out.println("received from client: " + inputLine);
                inputLine=in.readLine();
        }
        System.out.println("about to send to client");
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
        httpResponse+="MyHTTPServer";
        clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
    }
}
