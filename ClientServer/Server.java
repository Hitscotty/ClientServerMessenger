package ClientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * 
 * @author Jonathan Portorreal aka Scotty 
 * 
 */

public class Server {
	public static final int N      = 3;
	public static int n            = 0;
	public static int programStart = 0;

	ServerSocket server;

	ClientHandler[] clientHandler = new ClientHandler[N];
	Vector<String> messageList    = new Vector<String>(10);


	// private static long counter = 0;

	public static void main(String[] args) {
		Server server = new Server();

		server.init();

	}

	public void init() {
		try {
			server = new ServerSocket(8080);

			for (int i = 0; i < N; i++) {

				clientHandler[i] = new ClientHandler();

				Thread t         = new Thread(clientHandler[i]);

				t.start();

				n++;

			}

		} catch (IOException x) {
		}
		;

	}

	public class ClientHandler implements Runnable {

		String[] friendsList = new String[N];

		DataInputStream input;
		DataOutputStream output;

		boolean online = true;

		String name;

		public ClientHandler() {

			try {
				Socket socket = server.accept();

				input         = new DataInputStream(socket.getInputStream());
				output        = new DataOutputStream(socket.getOutputStream());

			} catch (IOException x) {
			}
			;
		}

		public void run() {
			while (true) {
				try {
					String message = messageProtocal(input.readUTF());

					for(int i = 0; i < n; i++){
						if(messageList.size() < N && isNotInMessageList(clientHandler[i])){
							clientHandler[i].output.writeUTF(name + ": " + message);
						}
					}
					
					}
				catch (IOException x) {
				}
				
			}
		}

		// __________________________________________________________________________________________________
		// 										additional methods
		// __________________________________________________________________________________________________

		/**
		 * Put everyone into the messageList	
		 */
		public void populate(){
			for(int i = 0; i < n; i++){
				messageList.add(clientHandler[i].name);
			}
		

		}
		
		// __________________________________________________________________________________________________

		/**
		 * Message list contains everyone. If not in list of everyone this means you must be inside a friendslist
		 * so message only if not in message List.
		 * Returns true if you are in the friendsList and false if you are not in the friendslist by using messageList 
		 * to make logical assumptions of who is and isnt in friendsList.
		 * @param client
		 * @return
		 */
		public boolean isNotInMessageList(ClientHandler client){
			boolean not = true;
			for(int i = 0; i < messageList.size(); i++){
				if(client.name == messageList.get(i)){
					not = false;
				}
			}
			
			return not;
		}
		
		// __________________________________________________________________________________________________

		/**
		 * Checks if passed in clientHandler is able to chat with another
		 * by using the clientHandlers copy of the GUIClient's friendsList
		 * @param client
		 * @return
		 */
		public boolean chatsWith(ClientHandler client) {
			boolean willChat = false;
			
			for (String name : friendsList) {
				if (client.name == name) {
					willChat = true;
				}
			}

			return willChat;
		}

		// __________________________________________________________________________________________________

		/**
		 * special characters in message can elicit program protocals
		 * 
		 * @param message
		 * @return
		 */
		public String messageProtocal(String message) {
			String[] packet  = message.split(":");

			//int size         = Integer.parseInt(packet[0]);

			String[] names   = packet[0].split(",");
			updateFL(names);

			name = packet[1];
			
			return packet[2];

		}
		// __________________________________________________________________________________________________


		/**
		 * uses packet information to put each GUIChat's friendList into the 
		 * ClientHandlers friendList
		 * @param names
		 */
		public void updateFL(String[] names) {

			for (int i = 0; i < names.length; i++) {
				friendsList[i] = names[i];
				//messageList.remove(names[i]);
			}
			
		}
		
		// __________________________________________________________________________________________________


		/**
		 * takes input message and searches for the sections that holds the name
		 * returns name
		 * 
		 * @param m
		 * @return
		 */
		public String getUser(String m) {
			int size = m.length();
			char end = ':';
			for (int i = 0; i < size; i++) {
				if (m.charAt(i) == end) {
					m = m.substring(0, i);
					break;
				}
			}
			return m;
		}

		// __________________________________________________________________________________________________

		/**
		 * checks to see if the current client is offline, if the client is
		 * offline the client "exists" but is not connected
		 * 
		 * @param client
		 * @return
		 */
		public boolean doesClientExist(ClientHandler client) {
			for (ClientHandler clients : clientHandler) {
				if (clients.name == client.name) {
					return true;
				}
			}
			return false;
		}
	}

	
	// __________________________________________________________________________________________________

	/**
	 * remove client does not remove a client, it simply changes a clients
	 * status to offline. Since we are using arrays clients once connected can
	 * never be deleted
	 * 
	 * @param name
	 */
	public void removeClient(String name) {
		for (int i = 0; i < n; i++) {
			if (clientHandler[i].name == name) {
				clientHandler[i].online = false;
				decreasen(i);
				return;
			}
		}
	}

	/**
	 * decreases n value so that the output message loops only through the list
	 * of online people and pushes offline members to end of array
	 * 
	 * @param c
	 */
	public void decreasen(int c) {
		for (int i = c; i < n - 1; i++) {
			clientHandler[i] = clientHandler[i + 1];
		}
		n--;
	}
}
