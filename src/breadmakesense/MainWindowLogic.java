package breadmakesense;

import javax.swing.Timer;

import javafx.application.Platform;

public class MainWindowLogic {
	
	static Timer autoClickTimer;
	Timer uploadServerDataTimer;
	
	public static double breads;
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
	
	public static void uploadServerData() {
		
	}

}
