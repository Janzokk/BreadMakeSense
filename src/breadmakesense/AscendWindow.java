package breadmakesense;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
* Class used to create the ascend section, includes a window and the logic to "ascend" in the game
* @author Jan Pérez, Joel Ferrer
* @version 1.0
*/
public class AscendWindow {
//Global variables declaration
	static float ascendPreview;

	static Scene mainScene;
	static BorderPane bPane;

	static Stage mainStage;

	static Label question;
	static Label ascendDefinition;

	static VBox questionVBox;

	static Button confirmationButton;
	/**
	* Initialize the window and his components
	*/
	public static void inicialize() {

		mainStage = new Stage();
		questionVBox = new VBox();

		bPane = new BorderPane();
		mainScene = new Scene(bPane, 550, 200);
		mainStage.setTitle("Ascend");
		mainStage.setResizable(false);
		//Content
		question = new Label("");
		ascendDefinition = new Label(
				"The ascend value multiplies the number of breads you produce from all sources.\nThis includes clicks and buildings.\nThe default ascend value is 1%.");

		confirmationButton = new Button("I KNOW WHAT I AM DOING");

		questionVBox.setAlignment(Pos.CENTER);
		questionVBox.getChildren().addAll(question, ascendDefinition, confirmationButton);

		VBox.setMargin(question, new Insets(0, 0, 20, 0));
		VBox.setMargin(ascendDefinition, new Insets(0, 0, 20, 0));

		bPane.setCenter(questionVBox);

		mainStage.setScene(mainScene);
		//If they hit the button it does all the necessaries things to ascend
		confirmationButton.setOnAction(e -> {
			//Changes the value
			MainWindowLogic.ascend += ascendPreview;
			//Refresh the Top Label
			MainWindowFX.refreshTopLabel();
			//Restarts the data
			removeData();
			MainWindowFX.refreshBreadsSecond();
			mainStage.close();
			LoginWindowLogic.logger.info("Ascend completed");
		});

	}
	/**
	* Gets the ascend value correspondent to the breads the user have
	* and shows it
	*/
	public static void show() {

		ascendPreview = (float) (MainWindowLogic.breads / 40000);

		question.setText("Do you want to ascend? You are going to earn " + String.format("%.2f", ascendPreview)
				+ "% of ascend value.\nRemember that you are going to lose ALL PROGRESS.");

		mainStage.show();
	}
	/**
	* Removes all the data from the variables except the ascend value
	*/
	public static void removeData() {

		byte i;
		//The breads
		MainWindowLogic.breads = 0;
		MainWindowLogic.breadsClickAuto = 0;
		//The items and their prices
		for (i = 0; i < MainWindowLogic.items.length; i++) {
			MainWindowLogic.items[i] = 0;
			MainWindowLogic.itemsPrice[i] = MainWindowLogic.itemsPriceDefault[i];
			MainWindowFX.itemsButtons[i].setText(
					MainWindowLogic.items[i] + " " + MainWindowFX.itemsNames[i] + "\n" + MainWindowLogic.itemsPrice[i]);
		}

	}

}
