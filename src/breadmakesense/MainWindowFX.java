package breadmakesense;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainWindowFX extends Application {
	
	BorderPane bpane;
	Scene mainScene;
	HBox menuHBox;
	VBox clickerVBox;
	
	Label nBreads;
	
	Button donation;
	Button ascend;
	Button ranking;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		bpane = new BorderPane();
		mainScene = new Scene(bpane, 1280, 720);

		inferiorMenu();
		
		clickableBread();
		
		stage.setTitle("Bread Make Sense");
		
		stage.setScene(mainScene);
		stage.show();
		
		
	}
	
	public void clickableBread() {
		
		nBreads = new Label();
		nBreads.setFont(new Font("Arial",30));

		nBreads.setText("Number of breads: " + MainWindowLogic.getBreads());
		
		ImageView bread = new ImageView("file:///home/joel/eclipse-workspace/BreadMakeSense/bread.png");
		Bloom bloom = new Bloom();
		clickerVBox = new VBox();
		
		// This allows to click on transparent areas
		bread.setPickOnBounds(true);
		bread.setOnMouseClicked(e -> {
			MainWindowLogic.addClick();
			refreshBreads();
		});
		
		clickerVBox.setAlignment(Pos.CENTER);
		clickerVBox.getChildren().addAll(nBreads ,bread);
		clickerVBox.setPrefWidth(400);
		
		
		bpane.setLeft(clickerVBox);
		
	}
	
	public void refreshBreads() {
		nBreads.setText("Number of breads: " + MainWindowLogic.getBreads());
	}
	
	
	public void inferiorMenu() {
		
		menuHBox = new HBox();
		
		donation = new Button("DONATION");
		ascend = new Button("ASCEND");
		ranking = new Button("RANKING");
		
		donation.setOnAction(e -> System.out.println("A"));
		ascend.setOnAction(e -> System.out.println("B"));
		ranking.setOnAction(e -> System.out.println("C"));
		
		menuHBox.getChildren().addAll(donation, ascend, ranking);
		menuHBox.setAlignment(Pos.CENTER);
		bpane.setBottom(menuHBox);
		
		
	}

}
