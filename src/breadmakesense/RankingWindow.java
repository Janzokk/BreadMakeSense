package breadmakesense;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RankingWindow {

	static Stage mainStage;

	static TableView rankingTable;
	static Scene rankingScene;

	static BorderPane bPane;

	static VBox rankingVBox;

	static PreparedStatement rankingStmt;

	static ObservableList<RankingUser> observableUserRanking;

	public static void inicialize() {

		mainStage = new Stage();
		bPane = new BorderPane();
		rankingVBox = new VBox();
		rankingTable = new TableView();

		rankingScene = new Scene(bPane, 300, 350);
		mainStage.setTitle("Best bread makers of all time");

		mainStage.setScene(rankingScene);

		bPane.setCenter(rankingVBox);
		
		rankingVBox.getChildren().add(rankingTable);

		prepareResultSet();

		// Prepare the table view
		observableUserRanking = FXCollections.observableArrayList();

		TableColumn col1 = new TableColumn("Position");
		TableColumn col2 = new TableColumn("User");
		TableColumn col3 = new TableColumn("Legacy breads");
		
		col1.setCellValueFactory(new PropertyValueFactory<RankingUser, String>("position"));
		col2.setCellValueFactory(new PropertyValueFactory<RankingUser, String>("username"));
		col3.setCellValueFactory(new PropertyValueFactory<RankingUser, String>("legacyBread"));

		rankingTable.getColumns().addAll(col1, col2, col3);

	}

	private static void prepareResultSet() {

		try {
			rankingStmt = LoginWindowLogic.con
					.prepareStatement("SELECT username, legacy_bread FROM users ORDER BY legacy_bread DESC");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void show() {
		
		try {
			observableUserRanking.clear();
			rankingTable.getItems().clear();
			ResultSet ranking = rankingStmt.executeQuery();
			ranking.next();
			for (int i = 0; i < 10; i++) {
				observableUserRanking.add(new RankingUser((byte) (i+1), ranking.getString(1), (long) ranking.getDouble(2)));
				ranking.next();
			}
			rankingTable.getItems().addAll(observableUserRanking); 
		} catch (SQLException e) {
			e.printStackTrace();
		}

		mainStage.show();

	}

}
