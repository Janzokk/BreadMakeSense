package bread;

import javafx.application.Application;
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
		
		pStage.show();
	}
	
	public void loginSection(){
		
		Button subBut = new Button("Submit");
		
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
		
		loginVBox.setMargin(pass,  new Insets(20, 0, 20 ,0));
		loginVBox.setMargin(tfUser,  new Insets(20, 0, 20 ,0));
		
		subBut.setOnAction(e -> {
			LoginWindowLogic.attempLogin(tfUser.getText(), pass.getText());
		});
	}
	
	public void serverSection() {
		
		
		Label introServerBreads = new Label("Together we have created a total of: ");
		Label serverBreads = new Label(""+LoginWindowLogic.serverPuntuation());

		serverBreads.setFont(new Font("Arial", 30));
		
		serverBreadsVBox = new VBox();
		
		serverBreadsVBox.setAlignment(Pos.CENTER);
		
		serverBreadsVBox.setPrefWidth(640);
		
		serverBreadsVBox.getChildren().addAll(introServerBreads, serverBreads);
		
		bPane.setRight(serverBreadsVBox);
		
	}
}
