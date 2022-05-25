package breadmakesense;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginWindowFX extends Application {

	BorderPane bPane;

	VBox loginVBox;

	VBox serverBreadsVBox;

	Stage pStage;

	public static void main(String[] args) {

		launch(args);

	}

	public void start(Stage pStage) throws Exception {
		LoginWindowLogic.startLogic();

		bPane = new BorderPane();
		Scene loginScene = new Scene(bPane, 1280, 720);

		pStage.setTitle("Login - Bread Make Sense");
		pStage.setScene(loginScene);

		loginSection();
		serverSection();

		this.pStage = pStage;

		pStage.show();
	}

	public void loginSection() {

		Button subBut = new Button("Submit");
		subBut.setDisable(true);

		TextField tfUser = new TextField();
		TextField pass = new PasswordField();

		tfUser.setAlignment(Pos.CENTER);
		pass.setAlignment(Pos.CENTER);
		tfUser.setMaxWidth(160);
		pass.setMaxWidth(160);

		loginVBox = new VBox();

		loginVBox.setAlignment(Pos.CENTER);

		loginVBox.setPrefWidth(640);

		bPane.setLeft(loginVBox);

		loginVBox.getChildren().addAll(new Label("Username"), tfUser, new Label("Password"), pass, subBut);

		VBox.setMargin(pass, new Insets(20, 0, 20, 0));
		VBox.setMargin(tfUser, new Insets(20, 0, 20, 0));

		tfUser.setOnKeyTyped(e -> {
			subBut.setDisable(LoginWindowLogic.loginInCheck(tfUser.getText(), pass.getText()));
		});

		pass.setOnKeyTyped(e -> {
			subBut.setDisable(LoginWindowLogic.loginInCheck(tfUser.getText(), pass.getText()));
		});

		subBut.setOnAction(e -> {
			LoginWindowLogic.attempLogin(tfUser.getText(), pass.getText());
			if (LoginWindowLogic.incPass) {
				subBut.setText("Incorrect\npassword");

				delay(1500, () -> subBut.setText("Submit"));

			} else {
				subBut.setText("STARTING");
				subBut.setDisable(true);
				MainWindowFX init = new MainWindowFX();
				init.inicialize();
				pStage.hide();
			}

		});
	}

	/**
	 * Method to delay the application for certain milliseconds and then continue it
	 * with some action
	 * 
	 * @param millis       The milliseconds it has to wait
	 * @param continuation The action it has to perform next Code not original
	 *                     -> @author silvalli
	 */
	public static void delay(long millis, Runnable continuation) {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(event -> continuation.run());
		new Thread(sleeper).start();
	}

	public void serverSection() {

		Label introServerBreads = new Label("Together we have created a total of: ");
		Label serverBreads = new Label("" + LoginWindowLogic.serverPuntuation() + " breads");
		Label version = new Label("" + LoginWindowLogic.clientVersion);

		serverBreads.setFont(new Font("Arial", 30));

		serverBreadsVBox = new VBox();

		serverBreadsVBox.setAlignment(Pos.CENTER);

		serverBreadsVBox.setPrefWidth(640);

		serverBreadsVBox.getChildren().addAll(introServerBreads, serverBreads, version);

		bPane.setRight(serverBreadsVBox);

	}
}
