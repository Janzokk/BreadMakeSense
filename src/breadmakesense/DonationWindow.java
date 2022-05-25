package breadmakesense;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DonationWindow {

	static Scene mainScene;
	static BorderPane bPane;

	static Stage mainStage;

	static VBox donationVBox;
	
	static TextField toUserField;
	static TextField quantityField;
	
	static Button donate;
	
	public static void inicialize() {
		
		mainStage = new Stage();
		donationVBox = new VBox();
		bPane = new BorderPane();
		
		donate = new Button("SEND");
		
		mainScene = new Scene(bPane, 200, 200);
		mainStage.setTitle("BREADZUM");
		toUserField = new TextField();
		quantityField = new TextField();
		
		donationVBox.setAlignment(Pos.CENTER);
		donationVBox.getChildren().addAll(new Label("To user"), toUserField, new Label("Quantity"), quantityField, donate);
		
		donationVBox.setMaxWidth(160);
		
		VBox.setMargin(quantityField, new Insets(20,0,20,0));
		VBox.setMargin(toUserField, new Insets(20,0,20,0));
		
		bPane.setCenter(donationVBox);
		mainStage.setScene(mainScene);
		
		donate.setOnAction(e ->{
			
		});

	}
	
	public static void show() {
		
		mainStage.show();

	}

}
