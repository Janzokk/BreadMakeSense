package breadmakesense;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;

public class LoginWindowFX extends Application{
	
	

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane bpane = new BorderPane();
		Scene loginScene = new Scene(bpane, 620, 360);
		Scene world = new Scene(bpane, 620, 360);
		primaryStage.setTitle("BMS - Login");
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}


}
