package breadmakesense;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Timer;

import javafx.application.Platform;

public class MainWindowLogic {

	static Timer autoClickTimer;
	static Timer uploadServerDataTimer;

	public static double breads;
	public static double legacyBreads;
	public static float ascend = 1;
	public static int breadsClick = 1;
	public static float breadsClickAuto;
	public static float breadsPerSecond;

	static int[] items = new int[4];

	static int[] itemsPriceDefault = { 20, 500, 2500, 5000 };

	static int[] itemsPrice = { 20, 500, 2500, 5000 };

	public static void addClick() {

		breadsClick = (int) ((1 + (items[0] / 5) + (items[1] / 5) + (items[2] / 5) + (items[3] / 5)) * ascend);

		breads += breadsClick;
		legacyBreads += breadsClick;

	}

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

	public static void calculateBreadsSecond() {
		breadsPerSecond = breadsClickAuto * 240;
	}

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

	public static void initalizeUploadDataTimer() {

		uploadServerDataTimer = new Timer(10000, e -> {
			uploadServerData();
		});

		uploadServerDataTimer.start();

	}

	public static void autoClick() {
		// This way we can accurately produce the bread and refresh the counter that the
		// user sees smoothly
		breadsClickAuto = (float) (((items[0] * 0.001) + (items[1] * 0.04) + (items[2] * 0.016) + (items[3] * 0.064))
				* ascend);

		breads += breadsClickAuto;
		legacyBreads += breadsClickAuto;

	}

	public static void downloadServerData() {
		try {
			
		PreparedStatement usStmt = LoginWindowLogic.con.prepareStatement("select bread, ascend, legacy_bread from users where username = ?");
		
		PreparedStatement itStmt = LoginWindowLogic.con.prepareStatement("select i1, i2, i3, i4 from users where username = ?");
		
		usStmt.setString(1, LoginWindowLogic.username);
		
		itStmt.setString(1, LoginWindowLogic.username);
		
		ResultSet usRs = usStmt.executeQuery();
		ResultSet itRs = itStmt.executeQuery();
		
		usRs.next();
		itRs.next();
		
		breads = usRs.getDouble(1);
		
		ascend = usRs.getFloat(2);
		
		legacyBreads = usRs.getDouble(3);
		

		for (int i = 0; i < items.length; i++) {
			items[i] = itRs.getInt(i+1);
			for(int j = 0; j < items[i]; j++) {
				itemsPrice[i] *= 1.2;
			}
		}
		
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public static void uploadServerData() {
		try {
			PreparedStatement usStmt = LoginWindowLogic.con.prepareStatement("update users set bread = ?, legacy_bread = ?, ascend = ? where username = ?");
			
			PreparedStatement itStmt = LoginWindowLogic.con.prepareStatement("update users set i1 = ?, i2 = ?, i3 = ?, i4 = ? where username = ?");
			
			usStmt.setDouble(1, breads);
			usStmt.setDouble(2, legacyBreads);
			usStmt.setFloat(3, ascend);
			usStmt.setString(4, LoginWindowLogic.username);
			

			for(int i = 0; i<items.length; i++) {
				itStmt.setInt(i+1, items[i]);

			}
			itStmt.setString(5, LoginWindowLogic.username);
			
			usStmt.executeUpdate();
			itStmt.executeUpdate();
			
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
