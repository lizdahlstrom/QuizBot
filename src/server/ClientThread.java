package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientThread extends Thread {

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Quiz game;
	private String name;
	private Server server;
	private int score;
	private boolean isReady = false;

	public ClientThread(Socket socket, Server server) throws IOException, ClassNotFoundException {
		this.server = server;
		score = 0;
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.writeObject(new String(
				"Welcome to QuizBot Server! \nInstructions: .n to get a random question, .r to start quiz.\nType in the right answer within the time limit.\n2 players needed to play."));
		outputStream.flush();
		inputStream = new ObjectInputStream(socket.getInputStream());
		name = (String) inputStream.readObject();
		System.out.println("Connection established on port " + socket.getPort());
	}

	@Override
	public void run() {
		System.out.println("New clientthread running...");
		try {

			System.out.println("Name set to: " + name);

			while (true) {
				String ans = (String) inputStream.readObject();
				System.out.println(name + ": " + ans);
				server.broadCastMsg(name + ": " + ans);

				if (game != null) {
					if (ans.equalsIgnoreCase(".r")) {
						isReady = true;
						server.checkReadiness();
					}
					if (ans.equalsIgnoreCase(".n")) {
						game.loadGame();
					}
					if (ans.equalsIgnoreCase(".s")) {
						game.broadcastMsg("Current score: " + score);
					}
					if (ans.equalsIgnoreCase(game.getQuestion().getCorrect_answer())) {
						score++;
						game.broadcastMsg(this.name + " just scored 1 point! Well done.");
					}
				}
				if (ans.contains(".exit")) {
					server.removeClient(this);
					break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public void sendStr(String msg) throws IOException {
		outputStream.writeObject(msg);
		outputStream.flush();
	}

	public void setQuiz(Quiz game) {
		this.game = game;
	}

	public boolean getReadiness() {
		return isReady;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public int getScore() {
		return score;
	}

	public String getClientName() {
		return name;
	}
}
