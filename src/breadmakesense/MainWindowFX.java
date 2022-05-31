package breadmakesense;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Core window of the program, contains the principal game and the buttons to
 * access {@link AscendWindow}, {@link DonationWindow}, {@link RankingWindow}
 * 
 * @author Jan Pérez, Joel Ferrer
 * @version 1.0
 */
public class MainWindowFX {

	static Stage stage;

	static BorderPane bPane;
	static Scene mainScene;
	static HBox menuHBox;
	static VBox clickerVBox;
	static VBox shopVBox;
	/**
	 * Number of breads the user has
	 */
	static Label nBreads;
	/**
	 * Label that shows the breads per second
	 */
	static Label bSecond;

	static Button donation;
	static Button ascend;
	static Button ranking;
	static Button saveClose;
	static Button help;

	static Button[] itemsButtons;

	static Label topLabel;

	static TextArea infoTextArea;

	static String[] itemsInfo;
	static String[] itemsNames;

	/**
	 * Initializes the components of the main window.
	 */
	public void inicialize() {

		stage = new Stage();
		stage.setMinWidth(400);

		bPane = new BorderPane();
		mainScene = new Scene(bPane, 1280, 720);
		mainScene.getStylesheets().add("files//mainStsyle.css");

		itemsNames = new String[] { "WORKER", "BREAD TREE", "BREAD FARM", "FACTORY" };
		// Calls the other initialization methods
		inferiorMenu();
		shop();
		clickableBread();
		textArea();
		topLabel();

		MainWindowLogic.downloadServerData();
		// Since we downloaded the data we have to refresh the visual items that
		// requires them
		refreshAllItemButtons();
		refreshTopLabel();
		// Upon login we start the auto click timer (that is going to call a method for
		// doing one click) and the auto save timer that is going to upload the user
		// data to the server
		MainWindowLogic.initalizeAutoClickTimer();
		MainWindowLogic.initalizeUploadDataTimer();

		stage.setTitle("Bread Make Sense");

		stage.setScene(mainScene);
		// We inicialize the scenes of the inferior menu (help is an alert, not a scene)
		DonationWindow.inicialize();
		AscendWindow.inicialize();
		RankingWindow.inicialize();

		stage.show();
		
		// This piece of code is executed when the app is closed or when the system calls System.exit(). This saves the game
		// even if the user closes the app via the "X" button. Difficulting the task of
		// duplicating breads with the donation option

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				MainWindowLogic.uploadServerData();
			}
		}, "Close app save"));

		stage.setOnCloseRequest(e -> System.exit(0));

	}

	/**
	 * Inicialitzes the top label of the window.
	 */
	public void topLabel() {
		topLabel = new Label("User: " + LoginWindowLogic.username + " Ascend value: "
				+ String.format("%.2f", MainWindowLogic.ascend) + "% Breads per click: " + MainWindowLogic.breadsClick);
		topLabel.setPadding(new Insets(10));
		bPane.setTop(topLabel);
	}

	/**
	 * Refreshes the information of the top label.
	 */
	public static void refreshTopLabel() {
		topLabel.setText("User: " + LoginWindowLogic.username + " | Ascend value: "
				+ String.format("%.2f", MainWindowLogic.ascend) + "% | Breads per click: "
				+ MainWindowLogic.breadsClick);
	}

	/**
	 * Inicialitzes the left section of the window. Containing the clicable bread,
	 * the quantity and the breads per second.
	 */
	public void clickableBread() {

		nBreads = new Label();
		nBreads.setFont(new Font("Arial", 20));

		bSecond = new Label();
		bSecond.setFont(new Font("Arial", 16));

		bSecond.setText(String.format("Breads/s: %.2f", MainWindowLogic.breadsPerSecond));
		nBreads.setText("Number of breads: " + MainWindowLogic.breads);
		bSecond.setId("br");
		nBreads.setId("br");

		clickerVBox = new VBox();
		clickerVBox.setId("clickerSect");
		// Image of the clicable bread
		ImageView breadImg = new ImageView("assets//bread.png");
		// Action when clicking the bread
		breadImg.setOnMouseClicked(e -> {
			MainWindowLogic.addClick();
			refreshBreads();
		});

		clickerVBox.setAlignment(Pos.CENTER);
		clickerVBox.getChildren().addAll(nBreads, bSecond, breadImg);
		clickerVBox.setPrefWidth(400);

		bPane.setLeft(clickerVBox);

	}

	/**
	 * Inicialitzes the text area of the center of the window and the description of
	 * the items.
	 */
	public void textArea() {

		itemsInfo = new String[MainWindowLogic.items.length];
		// Description of the items
		itemsInfo[0] = "A minum wage worker that is not into any type of pressure!\n"
				+ "It's not much, but it's honest work.\n\n"
				+ "<<Please help me>> - A happy worker\n\nProduces honest work.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";
		itemsInfo[1] = "Breads come from trees.\nYou didn't know that?\n\n" + "<<>> - A bread tree\n\n"
				+ "Produces 4 times more breads than a worker.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";
		itemsInfo[2] = "Maybe you think that we collect wheat in order to make bread.\n"
				+ "Well, no, this farm produces pure bread from the bread plant.\n\n"
				+ "<<Photosynthesis>> - A bread plant\n\n" + "Produces 4 times more breads than a bread tree.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";
		itemsInfo[3] = "A bread factory, the peak of enginering, so cool.\n"
				+ "It has no workers in case you were wondering.\n\n"
				+ "<<Brrrrrrrrrrr>> - The machines inside the factory\n\n"
				+ "Produces 4 times more breads than a bread farm.\n\n"
				+ "Your clicks produce +1 for every 5 buildings of this type.";

		infoTextArea = new TextArea(LoginWindowLogic.loginInfo);
		// This way the user can't write on the text area.
		infoTextArea.setEditable(false);
		bPane.setCenter(infoTextArea);
		// Actions of the buttons
		itemsButtons[0].setOnMouseEntered(e -> {
			infoTextArea.setText(itemsInfo[0]);
		});

		itemsButtons[1].setOnMouseEntered(e -> {
			infoTextArea.setText(itemsInfo[1]);
		});

		itemsButtons[2].setOnMouseEntered(e -> {
			infoTextArea.setText(itemsInfo[2]);
		});

		itemsButtons[3].setOnMouseEntered(e -> {
			infoTextArea.setText(itemsInfo[3]);
		});

	}

	/**
	 * Inicialitzes the shop on the right of the window and his respective
	 * components.
	 */
	public void shop() {

		shopVBox = new VBox();
		shopVBox.setId("shop");
		// Initialization of the buttons
		itemsButtons = new Button[4];

		itemsButtons[0] = new Button(
				MainWindowLogic.items[0] + " " + itemsNames[0] + "\nPrice: " + MainWindowLogic.itemsPrice[0]);
		itemsButtons[1] = new Button(
				MainWindowLogic.items[1] + " " + itemsNames[1] + "\nPrice: " + MainWindowLogic.itemsPrice[1]);
		itemsButtons[2] = new Button(
				MainWindowLogic.items[2] + " " + itemsNames[2] + "\nPrice: " + MainWindowLogic.itemsPrice[2]);
		itemsButtons[3] = new Button(
				MainWindowLogic.items[3] + " " + itemsNames[3] + "\nPrice: " + MainWindowLogic.itemsPrice[3]);

		for (Button b : itemsButtons) {
			b.setPrefWidth(200);
			b.setAlignment(Pos.CENTER_LEFT);
		}
		// Actions of the buttons
		itemsButtons[0].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 0))
				infoTextArea.setText("You don't have enough breads!");
			itemsButtons[0].setText(
					MainWindowLogic.items[0] + " " + itemsNames[0] + "\nPrice: " + MainWindowLogic.itemsPrice[0]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		itemsButtons[1].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 1))
				infoTextArea.setText("You don't have enough breads!");
			itemsButtons[1].setText(
					MainWindowLogic.items[1] + " " + itemsNames[1] + "\nPrice: " + MainWindowLogic.itemsPrice[1]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		itemsButtons[2].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 2))
				infoTextArea.setText("You don't have enough breads!");
			itemsButtons[2].setText(
					MainWindowLogic.items[2] + " " + itemsNames[2] + "\nPrice:" + MainWindowLogic.itemsPrice[2]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		itemsButtons[3].setOnAction(e -> {
			if (!MainWindowLogic.buyItem((byte) 3))
				infoTextArea.setText("You don't have enough breads!");
			itemsButtons[3].setText(
					MainWindowLogic.items[3] + " " + itemsNames[3] + "\nPrice: " + MainWindowLogic.itemsPrice[3]);
			refreshBreadsSecond();
			refreshTopLabel();
		});

		Label shopText = new Label("Shop");
		shopText.setFont(new Font("Arial", 22));

		VBox.setMargin(shopText, new Insets(0, 0, 20, 0));
		// We add the buttons to the VBox
		shopVBox.getChildren().addAll(shopText, itemsButtons[0], itemsButtons[1], itemsButtons[2], itemsButtons[3]);
		shopVBox.setAlignment(Pos.CENTER);
		bPane.setRight(shopVBox);

	}

	/**
	 * Refreshes the number of breads the user sees on the top of the breads per
	 * second.
	 */
	public static void refreshBreads() {
		nBreads.setText("Number of breads: " + (long) MainWindowLogic.breads);
	}

	/**
	 * Refreshes the number of breads per second the user sees on the top of the
	 * bread.
	 */
	public static void refreshBreadsSecond() {
		MainWindowLogic.calculateBreadsSecond();
		bSecond.setText(String.format("Breads/s: %.2f", MainWindowLogic.breadsPerSecond));
	}

	/**
	 * Refreshes the information of every item shown in the buttons
	 */
	public static void refreshAllItemButtons() {
		for (int i = 0; i < itemsButtons.length; i++) {
			itemsButtons[i].setText(
					MainWindowLogic.items[i] + " " + itemsNames[i] + "\nPrice: " + MainWindowLogic.itemsPrice[i]);
		}
	}

	/**
	 * Inicialitzes the inferior menu and all his components
	 */
	public void inferiorMenu() {

		Alert helpBox = new Alert(AlertType.INFORMATION);
		helpBox.setTitle("How to play Bread Make Sense");
		helpBox.setHeaderText("So you don't know how to play");
		helpBox.setContentText(
				"You have to make bread. To make bread you have to click the bread. When you have enough breads you can buy upgrades (more than one) at the shop that clicks for you. That's it. Happy bread making.");
		helpBox.setGraphic(new ImageView("assets//breadHelpMenu.png"));

		menuHBox = new HBox();
		// Initialization of the buttons on the inferior menu
		donation = new Button("DONATION");
		ascend = new Button("ASCEND");
		ranking = new Button("RANKING");
		saveClose = new Button("SAVE & CLOSE");
		help = new Button("HELP");
		// Actions of the buttons
		donation.setOnAction(e -> DonationWindow.show());
		ascend.setOnAction(e -> AscendWindow.show());
		ranking.setOnAction(e -> RankingWindow.show());
		saveClose.setOnAction(e -> {
			// We simply call the thread that is executed when closing the program
			System.exit(0);
		});
		help.setOnAction(e -> helpBox.show());

		menuHBox.getChildren().addAll(donation, ascend, ranking, saveClose, help);
		menuHBox.setAlignment(Pos.CENTER);
		bPane.setBottom(menuHBox);

	}

}
