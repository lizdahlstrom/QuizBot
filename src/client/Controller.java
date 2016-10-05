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
	private Label lblTop;
	@FXML
	private Label lblTopD;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnDisconnect;

	public void setMain(App app) {
		this.app = app;
		txtChat.setWrapText(true);
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
			setConnectedStatus(true);
		} else {
			lblValidation.setText("Invalid input");
		}
	}

	public void disconnect() throws IOException {
		setConnectedStatus(false);
		System.out.println("closing connection...");
		app.getConnection().postMsg(".exit");
	}

	public void exitApp() throws IOException {
		setConnectedStatus(false);
		app.getConnection().postMsg(".exit");
		Platform.exit();
	}

	public void updateView(String str) {
		txtChat.appendText(str + "\n");
	}

	private void setConnectedStatus(boolean bool) {
		app.isConnected = bool;
		lblTop.setVisible(bool);
		lblTopD.setVisible(!bool);
	}
}
