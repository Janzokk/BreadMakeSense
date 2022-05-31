package breadmakesense;
/**
* Users from the ranking
*/
public class RankingUser {
	//Variables
	private byte position;
	private String username;
	private long legacyBread;
	/**
	 * Constructor totally parameterized
	 * @param position Position of the user on the leaderboard (from 1 to 10)
	 * @param username Name of the account of the user
	 * @param legacyBread The bread that the user has generated over all time
	 */
	public RankingUser(byte position, String username, long legacyBread) {
		this.position = position;
		this.username = username;
		this.legacyBread = legacyBread;
	}
	//Getters
	/**
	 * @return the position
	 */
	public byte getPosition() {
		return position;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the legacyBread
	 */
	public double getLegacyBread() {
		return legacyBread;
	}
	//Setters
	/**
	 * @param position the position to set
	 */
	public void setPosition(byte position) {
		this.position = position;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param legacyBread the legacyBread to set
	 */
	public void setLegacyBread(long legacyBread) {
		this.legacyBread = legacyBread;
	}
	

}
