package breadmakesense;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RankingWindow {

	static Stage mainStage;

	static TableView ranking;
	static Scene rankingScene;

	static BorderPane bPane;

	static VBox rankingVBox;

	public static void inicialize() {

		mainStage = new Stage();
		bPane = new BorderPane();
		rankingVBox = new VBox();
		ranking = new TableView();

		rankingScene = new Scene(bPane, 400, 600);
		mainStage.setTitle("Best bread makers of all time");

		mainStage.setScene(rankingScene);

		bPane.setCenter(rankingVBox);

		TableColumn position = new TableColumn("Position");
		TableColumn user = new TableColumn("User");
		TableColumn legacyBreads = new TableColumn("Legacy breads");

		ranking.getColumns().addAll(position, user, legacyBreads);
		rankingVBox.getChildren().add(ranking);

	}

	public static void show() {

		mainStage.show();

	}

}
