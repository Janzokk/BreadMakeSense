package bread;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Timer;

import javafx.application.Platform;

public class MainWindowLogic {
	
	static Timer autoClickTimer;
	Timer uploadServerDataTimer;
	
	public static double breads = 10000;
	public static double legacyBreads;
	public static float ascend = 1;
	public static int breadsClick = 1;
	public static float breadsClickAuto;
	public static float breadsPerSecond;
	
	static int[] items = new int[4];
	
	static int[] itemsPriceDefault = {20,500,2500,5000};
	
	static int[] itemsPrice = {20,500,2500,5000};
	
	
	public static void addClick() {
		
		breadsClick = (int) ((1+(items[0]/5)+(items[1]/5)+(items[2]/5)+(items[3]/5))*ascend);
		
		breads += breadsClick;
		legacyBreads += breadsClick;
		
	}
	
	public static boolean buyItem(byte item) {
		if (breads >= itemsPrice[item]) {
			breads -= itemsPrice[item];
			items[item]++;
			itemsPrice[item] *= 1.2;
			breadsClick = (int) ((1+(items[0]/5)+(items[1]/5)+(items[2]/5)+(items[3]/5))*ascend);
			return true;
		}
		return false;
	}
	
	public static void calculateBreadsSecond() {
		breadsPerSecond = breadsClickAuto*240;
	}
	
	
	public static void initalizeAutoClickTimer() {
		
		autoClickTimer = new Timer(1, e -> {
			autoClick();
			//If we don't do this we get an error for trying to mix the thread of FX and Swing
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					MainWindowFX.refreshBreads();
				}
				
			});
			
		});
		
		autoClickTimer.start();
		
	}
	
	public static void autoClick()  {
		//This way we can accurately produce the bread and refresh the counter that the user sees smoothly
		breadsClickAuto = (float) (((items[0]*0.001)+(items[1]*0.04)+(items[2]*0.016)+(items[3]*0.064))*ascend);
		
		breads += breadsClickAuto;
		legacyBreads += breadsClickAuto;
		
	}
	
	public static void downloadServerData() {
		try {
			
		PreparedStatement usStmt = LoginWindowLogic.con.prepareStatement("select bread, ascend from users where username = ?");
		
		PreparedStatement itStmt = LoginWindowLogic.con.prepareStatement("select it.i1, it.i2, it.i3, it.i4 from users where username = ?");
		
		usStmt.setString(1, LoginWindowLogic.username);
		
		itStmt.setString(1, LoginWindowLogic.username);
		
		ResultSet usRs = usStmt.executeQuery();
		ResultSet itRs = itStmt.executeQuery();
		
		breads = usRs.getDouble(1);
		
		ascend = usRs.getFloat(2);
		
		int cs = 0;
		for (int i : items) {
			i = itRs.getInt(cs);
			for(int j = 0; j <  i; j++) {
				itemsPrice[cs] *= 1.2;
			}
			cs++;
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
			
			int cs = 1;
			for(int i = 0; i<items.length; i++) {
				itStmt.setInt(cs, i);
				cs++;
			}
			itStmt.setString(5, LoginWindowLogic.username);
			
			usStmt.executeQuery();
			itStmt.executeQuery();
			
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}