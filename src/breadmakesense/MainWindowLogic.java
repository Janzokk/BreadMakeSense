package breadmakesense;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Timer;

import javafx.application.Platform;

/**
 * Logic behind the {@link MainWindowFX}
 * 
 * @author Jan Pérez, Joel Ferrer
 * @version 1.0
 */
public class MainWindowLogic {

	static Timer autoClickTimer;
	static Timer uploadServerDataTimer;

	public static int userID;
	/**
	 * Variable that saves the quantity of breads the user has. While the user will
	 * only se an integer, internally we save the breads with decimals. This way the
	 * bread counter goes smoothly and we can calculate more complex operations like
	 * the and system.
	 */
	public static double breads;
	public static double legacyBreads;
	public static float ascend = 1;
	public static int breadsClick = 1;
	public static float breadsClickAuto;
	public static float breadsPerSecond;

	private final static int AUTO_SAVE_TIME = 60000;

	static int[] items = new int[4];

	static int[] itemsPriceDefault = { 20, 500, 2500, 5000 };

	static int[] itemsPrice = { 20, 500, 2500, 5000 };

	public static void addClick() {

		breadsClick = (int) ((1 + (items[0] / 5) + (items[1] / 5) + (items[2] / 5) + (items[3] / 5)) * ascend);

		breads += breadsClick;
		legacyBreads += breadsClick;

	}

	/**
	 * When the shop buttons are pressed and the amount of breads owned are
	 * sufficient buys an item.
	 * 
	 * @param item Id from the item
	 * @return True when the item has been buyed and False when not.
	 */
	public static boolean buyItem(byte item) {
		if (breads >= itemsPrice[item]) {
			breads -= itemsPrice[item];
			items[item]++;
			itemsPrice[item] *= 1.2;
			breadsClick = (int) ((1 + (items[0] / 5) + (items[1] / 5) + (items[2] / 5) + (items[3] / 5)) * ascend);
			return true;
		}
		return false;
	}

	/**
	 * Calculates the breads per second generated.
	 */
	public static void calculateBreadsSecond() {
		breadsPerSecond = breadsClickAuto * System.nanoTime() / 60000000;
	}

	/**
	 * Sum the breads per frame to the total breads owned.
	 */
	public static void initalizeAutoClickTimer() {

		autoClickTimer = new Timer(1, e -> {
			autoClick();
			// If we don't do this we get an error for trying to mix the thread of FX and
			// Swing
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					MainWindowFX.refreshBreads();
				}

			});

		});

		autoClickTimer.start();

	}

	/**
	 * Starts the timer to upload all the client data to the database automatically.
	 */
	public static void initalizeUploadDataTimer() {
		uploadServerDataTimer = new Timer(AUTO_SAVE_TIME, e -> {
			uploadServerData();
		});

		uploadServerDataTimer.start();

	}

	/**
	 * Calculates the amount of breads you own a frame
	 */
	public static void autoClick() {
		// This way we can accurately produce the bread and refresh the counter that the
		// user sees smoothly
		breadsClickAuto = (float) (((items[0] * 0.001) + (items[1] * 0.04) + (items[2] * 0.016) + (items[3] * 0.064))
				* ascend);

		breads += breadsClickAuto;
		legacyBreads += breadsClickAuto;
	}

	/**
	 * Download all the data from the server and insert it on the client values.
	 */
	public static void downloadServerData() {
		try {

			PreparedStatement usStmt = LoginWindowLogic.con
					.prepareStatement("select bread, ascend, legacy_bread from users where username = ?");

			PreparedStatement itStmt = LoginWindowLogic.con
					.prepareStatement("select i1, i2, i3, i4 from users where username = ?");

			PreparedStatement IDStmt = LoginWindowLogic.con.prepareStatement("select id from users where username = ?");

			usStmt.setString(1, LoginWindowLogic.username);

			itStmt.setString(1, LoginWindowLogic.username);

			IDStmt.setString(1, LoginWindowLogic.username);

			ResultSet usRs = usStmt.executeQuery();
			ResultSet itRs = itStmt.executeQuery();
			ResultSet IDRs = IDStmt.executeQuery();

			usRs.next();
			itRs.next();
			IDRs.next();

			userID = IDRs.getInt(1);

			breads = usRs.getDouble(1);

			ascend = usRs.getFloat(2);

			legacyBreads = usRs.getDouble(3);

			for (int i = 0; i < items.length; i++) {
				items[i] = itRs.getInt(i + 1);
				for (int j = 0; j < items[i]; j++) {
					itemsPrice[i] *= 1.2;
				}
			}

		} catch (SQLException sqle) {
			LoginWindowLogic.logger.warning("Can't download the data.\n" + sqle.getMessage());
		}
	}

	/**
	 * Upload all the client data to the database.qqqqqqqqqqqqqqqqqqqqqqq
	 */
	public static void uploadServerData() {
		try {
			PreparedStatement usStmt = LoginWindowLogic.con
					.prepareStatement("update users set bread = ?, legacy_bread = ?, ascend = ? where username = ?");

			PreparedStatement itStmt = LoginWindowLogic.con
					.prepareStatement("update users set i1 = ?, i2 = ?, i3 = ?, i4 = ? where username = ?");

			usStmt.setDouble(1, breads);
			usStmt.setDouble(2, legacyBreads);
			usStmt.setFloat(3, ascend);
			usStmt.setString(4, LoginWindowLogic.username);

			for (int i = 0; i < items.length; i++) {
				itStmt.setInt(i + 1, items[i]);

			}
			itStmt.setString(5, LoginWindowLogic.username);

			usStmt.executeUpdate();
			itStmt.executeUpdate();

			MainWindowFX.infoTextArea.setText("Game auto saved!");

		} catch (SQLException sqle) {
			LoginWindowLogic.logger.warning("Can't upload the data.\n" + sqle.getMessage());
		}
	}

}
