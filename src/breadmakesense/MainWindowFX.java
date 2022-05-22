package breadmakesense;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainWindowFX extends Application {

	BorderPane bPane;
	Scene mainScene;
	HBox menuHBox;
	VBox clickerVBox;
	VBox shopVBox;

	static Label nBreads;
	static Label bSecond;

	Button donation;
	Button ascend;
	Button ranking;
	
	Button[] itemsButtons;
	
	Label topLabel;

	static TextArea infoTextArea;

	String[] itemsInfo;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		bPane = new BorderPane();
		mainScene = new Scene(bPane, 1280, 720);

		inferiorMenu();
		shop();
		clickableBread();
		textArea();
		topLabel();
		MainWindowLogic.initalizeAutoClickTimer();

		stage.setTitle("Bread Make Sense");

		stage.setScene(mainScene);
		stage.show();

	}
	
	public void topLabel() {
		topLabel = new Label("User: Ascend value: Breads per click: " + MainWindowLogic.breadsClick);
		topLabel.setPadding(new Insets(10));
		bPane.setTop(topLabel);
	}
	
	public void refreshTopLabel() {
		topLabel.setText("User: Test user Ascend value: 20% Breads per click: " + MainWindowLogic.breadsClick);
	}

	public void clickableBread() {

		nBreads = new Label();
		nBreads.setFont(new Font("Arial", 20));

		bSecond = new Label();
		bSecond.setFont(new Font("Arial", 16));

		bSecond.setText(String.format("Breads/s: %.2f", MainWindowLogic.breadsPerSecond));
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

		bPane.setLeft(clickerVBox);

	}

	public void textArea() {

		itemsInfo = new String[MainWindowLogic.items.length];

		itemsInfo[0] = "A minum wage worker that is not into any type of pressure!\n"
				+ "It's not much, but it's honest work.\n\n"
				+ "<<Please help me>> - A happy worker\n\nProduces honest work.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";
		itemsInfo[1] = "Breads come from trees.\nYou didn't know that?\n\n"
				+ "<<>> - A bread tree\n\n"
				+ "Produces 4 times more breads than a worker.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";
		itemsInfo[2] = "Maybe you think that we collect wheat in order to make bread.\n"
				+ "Well, no, this farm produces pure bread from the bread plant.\n\n"
				+ "<<Photosynthesis>> - A bread plant\n\n"
				+ "Produces 4 times more breads than a bread tree.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";
		itemsInfo[3] = "A bread factory, the peak inginering, so cool.\n"
				+ "It has no workers in case you were wondering.\n\n"
				+ "<<Brrrrrrrrrrr>> - The machines inside the factory\n\n"
				+ "Produces 4 times more breads than a bread farm.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";

		infoTextArea = new TextArea();
		// This way the user can't write on the text area
		infoTextArea.setEditable(false);
		bPane.setCenter(infoTextArea);
		
		itemsButtons[0].setOnMouseEntered(e ->{
			infoTextArea.setText(itemsInfo[0]);
		});
		
		itemsButtons[1].setOnMouseEntered(e ->{
			infoTextArea.setText(itemsInfo[1]);
		});
		
		itemsButtons[2].setOnMouseEntered(e ->{
			infoTextArea.setText(itemsInfo[2]);
		});
		
		itemsButtons[3].setOnMouseEntered(e ->{
			infoTextArea.setText(itemsInfo[3]);
		});

	}

	public void shop() {
		shopVBox = new VBox();
		
		itemsButtons = new Button[4];

		itemsButtons[0] = new Button(MainWindowLogic.items[0] + " WORKER\n" + MainWindowLogic.itemsPrice[0]);
		itemsButtons[1] = new Button(MainWindowLogic.items[1] + " BREAD TREE\n" + MainWindowLogic.itemsPrice[1]);
		itemsButtons[2] = new Button(MainWindowLogic.items[2] + " BREAD FARM\n" + MainWindowLogic.itemsPrice[2]);
		itemsButtons[3] = new Button(MainWindowLogic.items[3] + " FACTORY\n" + MainWindowLogic.itemsPrice[3]);
		
		for (Button b : itemsButtons) {
			b.setPrefWidth(200);
			b.setAlignment(Pos.CENTER_LEFT);
		}

		itemsButtons[0].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 0)) infoTextArea.setText("You don't have enough breads!");
			itemsButtons[0].setText(MainWindowLogic.items[0] + " WORKER\n" + MainWindowLogic.itemsPrice[0]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		itemsButtons[1].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 1)) infoTextArea.setText("You don't have enough breads!");
			itemsButtons[1].setText(MainWindowLogic.items[1] + " BREAD TREE\n" + MainWindowLogic.itemsPrice[1]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		itemsButtons[2].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 2)) infoTextArea.setText("You don't have enough breads!");
			itemsButtons[2].setText(MainWindowLogic.items[2] + " BREAD FARM\n" + MainWindowLogic.itemsPrice[2]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		itemsButtons[3].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 3)) infoTextArea.setText("You don't have enough breads!");
			itemsButtons[3].setText(MainWindowLogic.items[3] + " FACTORY\n" + MainWindowLogic.itemsPrice[3]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		shopVBox.getChildren().addAll(itemsButtons[0],itemsButtons[1],itemsButtons[2],itemsButtons[3]);
		shopVBox.setAlignment(Pos.CENTER_LEFT);
		bPane.setRight(shopVBox);

	}

	public static void refreshBreads() {
		nBreads.setText("Number of breads: " + (long) MainWindowLogic.breads);
	}

	public static void refreshBreadsSecond() {
		MainWindowLogic.calculateBreadsSecond();
		bSecond.setText(String.format("Breads/s: %.2f", MainWindowLogic.breadsPerSecond));
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
		bPane.setBottom(menuHBox);

	}

}
