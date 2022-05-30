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
/**
* Class used to create the ranking section
*/
public class RankingWindow {
//Global variables declaration
	static Stage mainStage;

	static TableView<RankingUser> rankingTable;
	static Scene rankingScene;

	static BorderPane bPane;

	static VBox rankingVBox;

	static PreparedStatement rankingStmt;

	static ObservableList<RankingUser> observableUserRanking;
	
	/**
	* Initialize the window and the table so we can use them later
	*/
	@SuppressWarnings("unchecked")
	public static void inicialize() {

		mainStage = new Stage();
		bPane = new BorderPane();
		rankingVBox = new VBox();
		rankingTable = new TableView<RankingUser>();
		//Content
		rankingScene = new Scene(bPane, 300, 350);
		mainStage.setTitle("Best bread makers of all time");
		mainStage.setResizable(false);

		mainStage.setScene(rankingScene);

		bPane.setCenter(rankingVBox);
		
		rankingVBox.getChildren().add(rankingTable);

		prepareResultSet();

		// Prepare the table view
		observableUserRanking = FXCollections.observableArrayList();

		TableColumn<RankingUser, String> col1 = new TableColumn<RankingUser, String>("Position");
		TableColumn<RankingUser, String> col2 = new TableColumn<RankingUser, String>("User");
		TableColumn<RankingUser, String> col3 = new TableColumn<RankingUser, String>("Legacy breads");
		
		col1.setCellValueFactory(new PropertyValueFactory<RankingUser, String>("position"));
		col2.setCellValueFactory(new PropertyValueFactory<RankingUser, String>("username"));
		col3.setCellValueFactory(new PropertyValueFactory<RankingUser, String>("legacyBread"));

		rankingTable.getColumns().addAll(col1, col2, col3);

	}
	/**
	* Creates the resultSet that will show the users and their punctuation
	*/
	private static void prepareResultSet() {

		try {
			rankingStmt = LoginWindowLogic.con
					.prepareStatement("SELECT username, legacy_bread FROM users ORDER BY legacy_bread DESC");
		} catch (SQLException e) {
			LoginWindowLogic.logger.warning("Can't get the users info from server.\n" + e.getMessage());
		}

	}
	/**
	* Shows the ten first ranked users
	*/
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
			LoginWindowLogic.logger.warning("Can't get the users info from server.\n" + e.getMessage());
		}

		mainStage.show();

	}

}
