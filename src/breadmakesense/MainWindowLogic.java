package breadmakesense;

public class MainWindowLogic {
	
	private static long breads;
	
	public static void addClick() {
		breads++;
	}

	/**
	 * @return the breads
	 */
	public static long getBreads() {
		return breads;
	}

	/**
	 * @param breads the breads to set
	 */
	public static void setBreads(long breads) {
		MainWindowLogic.breads = breads;
	}

}
