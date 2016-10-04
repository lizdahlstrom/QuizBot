package server;

import java.io.IOException;
import java.util.ArrayList;

public class Quiz implements Runnable {

	private Question question;
	private ArrayList<ClientThread> clients;

	public Quiz(ArrayList<ClientThread> clients) {
		this.clients = clients;
		try {
			question = (new QuestionLoader().loadRandomQuestion());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setPlayers();
	}

	@Override
	public void run() {
		System.out.println("Game started");
	}

	public void loadGame() {
		try {
			question = (new QuestionLoader().loadRandomQuestion());
			broadcastMsg("This question is in the category: " + question.getCategory()
					+ "\nEnter .r to start or .n for a new question...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startGame() {
		try {
			broadcastMsg("Get ready! Starting quiz in 10s...");
			Thread.sleep(10000);
			broadcastMsg(question.getQuestion());
			switch (question.getDifficulty()) {
			case "easy":
				broadcastMsg("You have 20 seconds");
				setTimer(20);
				break;
			case "medium":
				broadcastMsg("You have 40 seconds");
				setTimer(40);
				break;
			case "hard":
				broadcastMsg("You have 1 minute");
				setTimer(60);
			default:
				setTimer(60);
				break;
			}
		} catch (Exception e) {
			System.out.println("Error showing question");
		}

	}

	public void broadcastMsg(String str) {
		clients.forEach(client -> {
			try {
				client.getOutputStream().writeObject("Server: " + str);
			} catch (IOException e) {
				System.out.println("Exception broadcasting");
				e.printStackTrace();
			}
		});
	}

	private void setTimer(int time) {

	}

	public void endGame(String name) {
		broadcastMsg(name + " is the winner!");
	}

	private void setPlayers() {
		clients.forEach(client -> {
			client.setQuiz(this);
		});
	}

	public Question getQuestion() {
		return question;
	}
}
