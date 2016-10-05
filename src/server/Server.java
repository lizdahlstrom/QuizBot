package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private int port = 8000;
	private ServerSocket serverSocket;
	private ArrayList<ClientThread> clients;
	private Quiz game;

	public Server() throws IOException, ClassNotFoundException {
		clients = new ArrayList<>();
		initServer();
	}

	public void initServer() throws IOException, ClassNotFoundException {

		if (!isPortInUse()) {
			serverSocket = new ServerSocket(port);

			while (true) {
				System.out.println("Server running...");
				System.out.println("Standing by...");
				Socket clientSocket = serverSocket.accept();
				if (clients.size() < 2) {
					ClientThread clientThread = new ClientThread(clientSocket, this);
					clients.add(clientThread);
					clientThread.start();
					broadCastMsg(
							clientThread.getClientName() + " has joined server, current clients: " + clients.size());
					if (clients.size() == 2) {
						// start game
						game = new Quiz(clients);
						Thread gameThread = new Thread(game);
						gameThread.start();
					}
				}
			}
		} else {
			System.out.println("Closing app...");
			System.exit(0);
		}
		serverSocket.close();
		System.out.println("Server closed");
	}

	private boolean isPortInUse() {
		try {
			new ServerSocket(port).close();
			return false;
		} catch (Exception e) {
			System.out.println("Server port is already in use!");
			return true;
		}
	}

	public void broadCastMsg(String msg) {
		clients.forEach(client -> {
			try {
				client.sendStr(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public void removeClient(ClientThread clientThread) {
		System.out.println(clientThread.getClientName() + " disconnected from server.");
		broadCastMsg(clientThread.getClientName() + " disconnected from server.");
		clients.remove(clientThread);
		clientThread = null;
	}

	public void checkReadiness() {
		clients.forEach(client -> {
			if (!client.getReadiness()) {
				return;
			}
		});
		game.startGame();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Server server = new Server();
	}
}
