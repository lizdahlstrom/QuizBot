package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {
	private String title = "QuizBot Application";
	private Socket socket;
	private Thread connThread;
	private Connection conn;

	private Controller mController;

	public void mainWindow(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(App.class.getResource("view/MainView.fxml"));
			AnchorPane root = loader.load();

			mController = loader.getController();
			mController.setMain(this);

			Scene scene = new Scene(root);
			primaryStage.setResizable(false);
			primaryStage.setTitle(title);
			primaryStage.setScene(scene);
			primaryStage.show();
			System.out.println("Main window loaded successfully...");
			primaryStage.setOnCloseRequest(e -> {
				try {
					if (conn != null)
						conn.postMsg(".exit");
				} catch (IOException e1) {

				}
			});

		} catch (IOException e) {
			System.out.println("Exception loading main window...");
			e.printStackTrace();
		}
	}

	public void connectToServer(String name, int port, String adress) {
		try {
			socket = new Socket(adress, port);
			conn = new Connection(name, socket, mController);
			connThread = new Thread(conn);
			connThread.start();
		} catch (UnknownHostException e) {
			System.out.println("Unknow host exception...");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainWindow(primaryStage);

	}

	public static void main(String[] args) {
		Application.launch(args);

	}

	public Connection getConnection() {
		return conn;
	}

}
