/*
 * Copyright (c) 2010-2017 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * Customized to implement a simple chat protocol designed by Phu Phung
 * for Secure Internet Application Development course.
 * The chat protocol is as follows (version 3):
 * From client to server:
 * - Upon connect to this server. The client must send the username with 
 *   format of: <JOIN>Username, otherwise, the chat will not be started for the client.
 * - Chat message must be sent with the format of: <CHAT>message
 * - To request exit the chat, send <EXIT>
 * - When a user is typing something, send <TYPE> message to the server.
 * From server to client:
 * - When a new client is joint with username (<JOIN>Username), 
 *	 the join info (Username + "entered the room") will be sent to all connected client
 	 Version 3 Update: The server will also send a updated list of all connected chat users to all clients.
 	 Format: <URLIST>Username1<br>Username2<br>...

 * - When a client sends a chat message (<CHAT>message), the message will be forwarded to all clients.
 * - When a client closes (<EXIT>, the leave info will be sent to all connected client.
  	 Version 3 Update: The server will also send a updated list of all connected chat users to all clients.
 	 Format: <URLIST>Username1<br>Username2<br>...

 * - When a client sends a <TYPE> message, send to all connected client the message: "<TYPING>" + Username + " is typing...""
 * 
 * 
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

//SIAD code
import java.util.*;


/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom" in connections().
 */
public class SIADChatServer extends WebSocketServer {
	
	Map<WebSocket, String> usernames = new HashMap<WebSocket, String>();
	public SIADChatServer( int port ) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
	}

	public SIADChatServer( InetSocketAddress address ) {
		super( address );
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		//this.sendToAll( "new connection: " + handshake.getResourceDescriptor() );
		System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		//this.sendToAll( conn + " has left the room!" );
		this.removeUsername(conn);
		sendListToAll();
		System.out.println( conn + " has left the room!. Reason:" + reason);
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		//this.sendToAll( message );
		System.out.println( conn + ": " + message );
		String username = getUsername(conn);
		String command = this.getCommand(message); 
		if(username!=null){

			if(command.equals("<CHAT>")){
				this.sendToAll(username + " says: " + getData(message));
			}else if (command.equals("<EXIT>")){
				conn.close();
			}else if(command.equals("<TYPE>")){
				this.sendToAll("<TYPING>" + username + " is typing ... ");
			}


		}else{
			//no username is found. set the username
			if(command.equals("<JOIN>")){
				String newusername = getData(message);
				this.setUsername(newusername,conn);
				this.sendToAll(newusername + " entered the room!");
				this.sendListToAll();

			}else{
				conn.send("[<JOIN>Username] is required!");
			}
		}
		
	}

	@Override
	public void onFragment( WebSocket conn, Framedata fragment ) {
		System.out.println( "received fragment: " + fragment );
	}

	public static void main( String[] args ) throws InterruptedException , IOException {
		WebSocketImpl.DEBUG = true;
		int port = 8080; 
		try {
			port = Integer.parseInt( args[ 0 ] );
		} catch ( Exception ex ) {
		}
		SIADChatServer s = new SIADChatServer( port );
		s.start();
		System.out.println( "ChatServer started on port: " + s.getPort() );

		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while ( true ) {
			String in = sysin.readLine();
			s.sendToAll( in );
			if( in.equals( "exit" ) ) {
				s.stop();
				break;
			}
		}
	}
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	/*@Override
	public void onStart() {
		System.out.println("Server started!");
	}*/

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll( String text ) {
		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				c.send( text );
			}
		}
	}
	private void sendListToAll(){
		this.sendToAll(getUsernameList());
	}
	private String getData(String data){
		//remove the comment and get message part
		if(data.isEmpty() || (data.length()< 6))
    		return null;
    	try{
    		String message = data.substring(6).trim();
    		return message;
    	}catch(Exception e) {
    		return null;
    	}


	}
	private void setUsername(String username, WebSocket connection){

		synchronized ( usernames ) {
			usernames.put(connection, username);
		}
	}
	private String getUsername(WebSocket connection){
		synchronized ( usernames ) {
			return usernames.get(connection);
		}
	}

	private String getUsernameList(){
		String list = "<URLIST>";
		synchronized ( usernames ) {
			for (WebSocket socket: usernames.keySet()) {
    			 list+=usernames.get(socket)+"<br>";
			}
		}
		return list;
	}

	private void removeUsername(WebSocket connection){
		String username;
		synchronized ( usernames ) {
			username=usernames.get(connection);
			usernames.remove(connection);
			

		}
		this.sendToAll( username + " has left the room!");
	}
	private String getCommand(String data){
 		/*The message must be in the format of <XXXX>DATA, where:
		* <XXXX> is the command and could be one of:
		* <JOIN>Username;<TYPE>;<EXIT>; <LIST>; <CHAT>Message; 
		* thus, we get the command part to handle accordingly 			
		*/  
		//must check that the data is longer than 6 character
   		if(data.isEmpty() || (data.length()< 6))
    		return "UNKNOWN";
    	try{
    		String command = data.substring(0,6).trim();
    		return command;
    	}catch(Exception e) {
    		return "UNKNOWN";
    	}
    }
}
