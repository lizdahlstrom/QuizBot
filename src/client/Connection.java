package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection implements Runnable {

	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Controller mController;
	private String name;

	public Connection(String name, Socket socket, Controller mController) {
		this.name = name;
		this.socket = socket;
		this.mController = mController;
	}

	@Override
	public void run() {
		try {
			System.out.println("Running connection");

			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			postMsg(name);
			while (true) {
				mController.updateView("> " + (String) inputStream.readObject());
			}

		} catch (UnknownHostException e) {
			System.out.println("Uknown host...");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void postMsg(String msg) throws IOException {
		outputStream.writeObject(msg);
		outputStream.flush();
	}
}
