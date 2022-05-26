package breadmakesense;

public class RankingUser {
	
	private byte position;
	private String username;
	private long legacyBread;
	
	public RankingUser(byte position, String username, long legacyBread) {
		this.position = position;
		this.username = username;
		this.legacyBread = legacyBread;
	}

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
