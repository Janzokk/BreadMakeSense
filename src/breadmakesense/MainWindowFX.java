package breadmakesense;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	VBox shopVBox;

	static Label nBreads;
	static Label bSecond;

	Button donation;
	Button ascend;
	Button ranking;

	Button i1;
	Button i2;
	Button i3;
	Button i4;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		bpane = new BorderPane();
		mainScene = new Scene(bpane, 1280, 720);

		inferiorMenu();
		shop();
		clickableBread();
		MainWindowLogic.initalizeAutoClickTimer();

		stage.setTitle("Bread Make Sense");

		stage.setScene(mainScene);
		stage.show();

	}

	public void clickableBread() {

		nBreads = new Label();
		nBreads.setFont(new Font("Arial", 20));
		
		bSecond = new Label();
		bSecond.setFont(new Font("Arial", 16));

		bSecond.setText(String.format("Breads/s: %.2f",MainWindowLogic.breads));
		nBreads.setText("Number of breads: " + MainWindowLogic.breads);

		ImageView bread = new ImageView("assets//bread.png");

		clickerVBox = new VBox();

		bread.setOnMouseClicked(e -> {
			MainWindowLogic.addClick();
			refreshBreads();
		});

		clickerVBox.setAlignment(Pos.CENTER);
		clickerVBox.getChildren().addAll(nBreads, bSecond, bread);
		clickerVBox.setPrefWidth(400);

		bpane.setLeft(clickerVBox);

	}
	
	public void textArea() {
		
	}

	public void shop() {
		shopVBox = new VBox();

		i1 = new Button(MainWindowLogic.itemsPrice[0] + " WORKER");
		i2 = new Button(MainWindowLogic.itemsPrice[1] + " BREAD TREE");
		i3 = new Button(MainWindowLogic.itemsPrice[2] + " BREAD FARM");
		i4 = new Button(MainWindowLogic.itemsPrice[3] + " FACTORY");

		i1.setOnAction(e -> {
			MainWindowLogic.buyItem((byte) 0);
			i1.setText(MainWindowLogic.itemsPrice[0] + " WORKER");
			refreshBreadsSecond();
		});
		
		i2.setOnAction(e -> {
			MainWindowLogic.buyItem((byte) 1);
			i2.setText(MainWindowLogic.itemsPrice[1] + " BREAD TREE");
			refreshBreadsSecond();
		});
		
		i3.setOnAction(e -> {
			MainWindowLogic.buyItem((byte) 2);
			i3.setText(MainWindowLogic.itemsPrice[2] + " BREAD FARM");
			refreshBreadsSecond();
		});
		
		i4.setOnAction(e -> {
			MainWindowLogic.buyItem((byte) 3);
			i4.setText(MainWindowLogic.itemsPrice[3] + " FACTORY");
			refreshBreadsSecond();
		});
		
		shopVBox.getChildren().addAll(i1, i2, i3, i4);
		shopVBox.setAlignment(Pos.CENTER_LEFT);
		bpane.setRight(shopVBox);

	}

	public static void refreshBreads() {
		nBreads.setText("Number of breads: " + (long) MainWindowLogic.breads);
	}
	
	public static void refreshBreadsSecond() {
		MainWindowLogic.calculateBreadsSecond();
		bSecond.setText(String.format("Breads/s: %.2f",MainWindowLogic.breads));
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
