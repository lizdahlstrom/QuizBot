package client;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

	private App app;

	// Views
	@FXML
	private TextField txtInput;
	@FXML
	private TextArea txtChat;
	@FXML
	private ListView<String> clientList;
	@FXML
	private Button btnSubmit;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtAdress;
	@FXML
	private TextField txtPort;
	@FXML
	private Label lblValidation;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnDisconnect;

	public void setMain(App app) {
		this.app = app;
	}

	public void submit() {
		try {
			app.getConnection().postMsg(txtInput.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		txtInput.clear();
	}

	public void connect() {
		lblValidation.setText("");
		String port = txtPort.getText();
		if (port.matches("[0-9]+") && port.length() > 2 && port.length() < 5) {
			app.connectToServer(txtUsername.getText(), Integer.parseInt(port), txtAdress.getText());
		} else {
			lblValidation.setText("Invalid input");
		}
	}

	public void disconnect() {
		System.out.println("closing connection...");
	}

	public void exitApp() throws IOException {
		app.getConnection().postMsg(".exit");
		Platform.exit();
	}

	public void updateView(String str) {
		System.out.println("Updating txtChat");
		txtChat.appendText(str + "\n");

	}
}
