import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

class MyHTTPServerThread  extends Thread {
    private Socket clientSocket = null;
 
    MyHTTPServerThread(Socket clientSocket) {
        super("MyHTTPServerThread");
        this.clientSocket = clientSocket;
    }
     
    void run() {
 
        try {
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

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

        