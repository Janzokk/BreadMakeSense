package breadmakesense;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AscendWindow {

	static float ascendPreview;

	static Scene mainScene;
	static BorderPane bPane;

	static Stage stage;

	static Label question;
	static Label ascendDefinition;

	static VBox questionVBox;

	static Button confirmationButton;

	public static void inicialize() {

		stage = new Stage();
		questionVBox = new VBox();

		bPane = new BorderPane();
		mainScene = new Scene(bPane, 550, 200);

		question = new Label("");
		ascendDefinition = new Label(
				"The ascend value multiplies the number of breads you produce from all sources.\nThis includes clicks and buildings.\nThe default ascend value is 1%.");

		confirmationButton = new Button("I KNOW WHAT I AM DOING");

		questionVBox.setAlignment(Pos.CENTER);
		questionVBox.getChildren().addAll(question, ascendDefinition, confirmationButton);

		VBox.setMargin(question, new Insets(0, 0, 20, 0));
		VBox.setMargin(ascendDefinition, new Insets(0, 0, 20, 0));

		bPane.setCenter(questionVBox);

		stage.setScene(mainScene);

		confirmationButton.setOnAction(e -> {
			MainWindowLogic.ascend += ascendPreview;
			MainWindowFX.refreshTopLabel();
			removeData();
			MainWindowFX.refreshBreadsSecond();
			stage.close();
		});

	}

	public static void show() {

		ascendPreview = (float) (MainWindowLogic.breads / 10000);

		question.setText("Do you want to ascend? You are going to earn " + String.format("%.2f", ascendPreview)
				+ "% of ascend value.\nRemember that you are going to lose ALL PROGRESS.");

		stage.show();
	}

	public static void removeData() {

		byte i;

		MainWindowLogic.breads = 0;
		MainWindowLogic.breadsClickAuto = 0;

		for (i = 0; i < MainWindowLogic.items.length; i++) {
			MainWindowLogic.items[i] = 0;
			MainWindowLogic.itemsPrice[i] = MainWindowLogic.itemsPriceDefault[i];
			MainWindowFX.itemsButtons[i].setText(
					MainWindowLogic.items[i] + " " + MainWindowFX.itemsNames[i] + "\n" + MainWindowLogic.itemsPrice[i]);
		}

	}

}
