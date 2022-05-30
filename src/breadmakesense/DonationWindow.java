package breadmakesense;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
* Class used to create the donation section
*/
public class DonationWindow {
//Global variables declaration
	static Scene mainScene;
	static BorderPane bPane;

	static Stage mainStage;

	static VBox donationVBox;

	static TextField toUserField;
	static TextField quantityField;

	static Button donate;

	static PreparedStatement checkUserStmt;
	static PreparedStatement breadUpdateStmt;
	static PreparedStatement transactionStmt;
	
	static Alert transactionInfo;
	static Alert transactionErrorInfo;
	static Alert numberParseError;
	/**
	* Initialize the window
	*/
	public static void inicialize() {

		mainStage = new Stage();
		donationVBox = new VBox();
		bPane = new BorderPane();

		//Content
		donate = new Button("SEND");

		mainScene = new Scene(bPane, 200, 200);
		mainStage.setTitle("BREADZUM");
		mainStage.setResizable(false);
		toUserField = new TextField();
		quantityField = new TextField();

		donationVBox.setAlignment(Pos.CENTER);
		donationVBox.getChildren().addAll(new Label("To user"), toUserField, new Label("Quantity"), quantityField,
				donate);

		donationVBox.setMaxWidth(160);

		VBox.setMargin(quantityField, new Insets(20, 0, 20, 0));
		VBox.setMargin(toUserField, new Insets(20, 0, 20, 0));

		bPane.setCenter(donationVBox);
		mainStage.setScene(mainScene);
		//If they hit the button the transaction starts
		donate.setOnAction(e -> {
			doTransaction();
		});
		//Makes the transaction
		try {
			checkUserStmt = LoginWindowLogic.con.prepareStatement("SELECT username, id FROM users WHERE username = ?");
			breadUpdateStmt = LoginWindowLogic.con.prepareStatement("UPDATE users SET bread = bread + ? WHERE username = ?");
			transactionStmt = LoginWindowLogic.con.prepareStatement(
					"INSERT INTO transactions (trans_time, donator, recipient, bread) VALUES (NOW(), ?,?,?)");
			LoginWindowLogic.logger.info("Donation completed");
		} catch (SQLException e1) {
			LoginWindowLogic.logger.warning("Can't make the transaction.\n"+ e1.getMessage());
		}
		//Alerts for when the transaction doesn't go well
		transactionInfo = new Alert(AlertType.INFORMATION);
		transactionInfo.setTitle("WOW");
		transactionInfo.setHeaderText("That is so generous!");

		transactionErrorInfo = new Alert(AlertType.WARNING);
		transactionErrorInfo.setTitle("You didn't put that right");
		transactionErrorInfo.setHeaderText("You are doing something wrong");
		
		numberParseError = new Alert(AlertType.ERROR);
		numberParseError.setTitle("No, you can't do that");
		numberParseError.setHeaderText("What are you doing");
	}
	/**
	* Makes the transaction between the local user and a user from the server
	*/
	public static void doTransaction() {
		try {
			
			double quantity = Double.parseDouble(quantityField.getText());
			
			checkUserStmt.setString(1, toUserField.getText());
			ResultSet resultCheckUser = checkUserStmt.executeQuery();
			//Detects if the data from the transaction is valid (user exists, no more breads than you have, etc.)
			if (!resultCheckUser.next()) {
				transactionErrorInfo.setContentText("This user doesn't exists");
				transactionErrorInfo.show();
			} else if (toUserField.getText().equals(LoginWindowLogic.username)) {
				transactionErrorInfo.setContentText("You can't donate to yourself");
				transactionErrorInfo.show();
			} else if (MainWindowLogic.breads < quantity) {
				transactionErrorInfo.setContentText("You don't have enough breads");
				transactionErrorInfo.show();
			} else if (0 > quantity) {
				transactionErrorInfo.setContentText("You can't steal bread from other people.\nPlus it's ilegal.");
				transactionErrorInfo.show();
			} else if (0 == quantity) {
				transactionErrorInfo.setContentText("Those are your life savings? Impresive...");
				transactionErrorInfo.show();
			} else {
			//Transfers the breads
				breadUpdateStmt.setLong(1, Long.parseLong(quantityField.getText()));
				breadUpdateStmt.setString(2, toUserField.getText());
				breadUpdateStmt.executeUpdate();

				transactionStmt.setInt(1, MainWindowLogic.userID);
				transactionStmt.setInt(2, resultCheckUser.getInt(2));
				transactionStmt.setLong(3, Long.parseLong(quantityField.getText()));
				transactionStmt.executeUpdate();

				MainWindowLogic.breads -= quantity;

				transactionInfo.setContentText("Transaction made correctly");
				transactionInfo.show();
			}

		} catch (SQLException e) {
			
			LoginWindowLogic.logger.warning("Can't get data from the server.\n" + e.getMessage());
			
		} catch (NumberFormatException nfe) {
			
			numberParseError.setContentText("You have to put a number, you can't donate "+ quantityField.getText() + " breads.");
			numberParseError.show();
			
			LoginWindowLogic.logger.warning("Invalid number donated.\n" + nfe.getMessage());
		}
	}
	/**
	* Shows the stage
	*/
	public static void show() {

		mainStage.show();

	}

}
