package breadmakesense;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Creates the visual part of the "log in" window with JavaFX.
 * 
 * @author Jan Pérez, Joel Ferrer
 * @version 1.0
 */
public class LoginWindowFX extends Application {
//Global variables declaration
	static BorderPane bPane;

	static VBox loginVBox;

	static VBox serverBreadsVBox;
	static HBox buttonsVBox;

	static Stage pStage;

	static Scene loginScene;
	/**
	 * Submit button
	 */
	static Button subBut;
	static Button helpBut;

	static TextField tfUser;

	static TextField pass;

	static Alert help;

	/**
	 * Main method
	 */
	public static void main(String[] args) {

		launch(args);

	}

	/**
	 * Starts the JavaFX application
	 */
	public void start(Stage pStage) throws Exception {
		LoginWindowLogic.startLogic();

		bPane = new BorderPane();
		loginScene = new Scene(bPane, 1280, 720);

		pStage.setTitle("Login - Bread Make Sense");
		pStage.setResizable(false);
		pStage.setScene(loginScene);

		loginSection();
		serverSection();

		LoginWindowFX.pStage = pStage;

		pStage.getIcons().add(new Image("assets//bread.png"));

		loginScene.getStylesheets().add("files//style.css");
		pStage.show();
	}

	/**
	 * Builds the left part of the login screen (The one that lets you properly log
	 * in)
	 */
	private static void loginSection() {
		// Create a button for submitting and disable it
		subBut = new Button("Submit");
		subBut.setDisable(true);

		helpBut = new Button("Help");
		helpBut.setOnAction(e -> {
			help.show();
		});

		helpBut.setMinWidth(64);

		buttonsVBox = new HBox();
		buttonsVBox.getChildren().setAll(subBut, helpBut);
		buttonsVBox.setAlignment(Pos.CENTER);
		HBox.setMargin(subBut, new Insets(0, 30, 0, 0));

		// We create the alert that shows the user/passwd policy

		help = new Alert(AlertType.INFORMATION);
		help.setHeaderText("User and password policy");
		help.setContentText("User:\n\n" + "-Minium four characters\n" + "\nPassword:\n\n" + "-Minimum four characterss\n"
				+ "-At least one uppercase letter and one lowercase letter\n" + "-At least one number\n\n"
						+ "If you want to reset your password contact:\n\n"
						+ "jferrerr@inscastellet.cat\n"
						+ "jperezg@inscastellet.cat\n\n"
						+ "Made by Joel Ferrer & Jan Pérez - 2022 - Ver. " + LoginWindowLogic.clientVersion);

		tfUser = new TextField();
		pass = new PasswordField();
		// Put everything in the middle and ajust the width
		tfUser.setAlignment(Pos.CENTER);
		pass.setAlignment(Pos.CENTER);
		tfUser.setMaxWidth(160);
		pass.setMaxWidth(160);

		loginVBox = new VBox();
		loginVBox.setId("left");

		loginVBox.setAlignment(Pos.CENTER);

		loginVBox.setPrefWidth(640);

		bPane.setLeft(loginVBox);

		loginVBox.getChildren().addAll(new Label("Username"), tfUser, new Label("Password"), pass, buttonsVBox);

		VBox.setMargin(pass, new Insets(20, 0, 20, 0));
		VBox.setMargin(tfUser, new Insets(20, 0, 20, 0));

		// When a key is put on any of the text field it checks if it meets the
		// standarts of username/password so it make the button available
		tfUser.setOnKeyTyped(e -> {
			subBut.setDisable(LoginWindowLogic.loginInCheck(tfUser.getText(), pass.getText()));
		});

		pass.setOnKeyTyped(e -> {
			subBut.setDisable(LoginWindowLogic.loginInCheck(tfUser.getText(), pass.getText()));
		});
		// When the button is pressed it tries to log in with the username/password that
		// are in the fields
		subBut.setOnAction(e -> {
			attemptLoginProcess();
		});
		// Can also try to log in with the "Enter" button
		loginScene.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.ENTER && !subBut.isDisabled())
				attemptLoginProcess();
		});

	}

	/**
	 * Method that calls the Logic method for attemping the login
	 */
	private static void attemptLoginProcess() {
		LoginWindowLogic.attempLogin(tfUser.getText(), pass.getText());
		if (LoginWindowLogic.incPass) {
			subBut.setText("Incorrect\npassword");

			delay(1500, () -> subBut.setText("Submit"));

		} else {
			// If everything correct it changes the button message, disables it, start the
			// Main scene and close the Login scene
			subBut.setText("STARTING");
			subBut.setDisable(true);
			MainWindowFX.inicialize();
			pStage.hide();
		}
	}

	/**
	 * Method to delay the application for certain milliseconds and then continue it
	 * with some action
	 * 
	 * @param millis       The milliseconds it has to wait
	 * @param continuation The action it has to perform next Code not original
	 *                     -> @author silvalli
	 */
	private static void delay(long millis, Runnable continuation) {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
					LoginWindowLogic.logger.warning("Can't sleep the thread.\n" + e.getMessage());
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(event -> continuation.run());
		new Thread(sleeper).start();
	}

	/**
	 * Builds the right part of the login scene (the one that says the breads info)
	 */
	private void serverSection() {

		Label introServerBreads = new Label("Together we have created a total of: ");
		Label serverBreads = new Label("" + String.format("%.0f", LoginWindowLogic.serverPuntuation()) + " breads");
		Label version = new Label("" + LoginWindowLogic.clientVersion);

		serverBreads.setFont(new Font("Arial", 30));

		serverBreadsVBox = new VBox();

		serverBreadsVBox.setAlignment(Pos.CENTER);

		serverBreadsVBox.setPrefWidth(640);
		// We add all the labels to the VBox of the server section
		serverBreadsVBox.getChildren().addAll(introServerBreads, serverBreads, version);

		bPane.setRight(serverBreadsVBox);
	}
}
