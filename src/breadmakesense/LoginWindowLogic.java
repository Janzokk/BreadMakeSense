package breadmakesense;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * This class is the one with the logic used to "log in" into the server
 * 
 * @author Jan Pérez, Joel Ferrer
 * @version 1.0
 */
public class LoginWindowLogic {
	// Global variables declaration
	static Connection con;
	static Statement stmt;
	static ResultSet getUname;
	static FileHandler handler;
	static Logger logger;
	static FileReader fr;
	static BufferedReader br;
	/**
	 * We save the key/value of the file configuration here
	 */
	static HashMap<String, String> hm;
	/**
	 * This parameter is used to compare the version of the client and the server. This parameter should only be changed via coding when a new version is being developed
	 */
	static String clientVersion = "1.0";
	static boolean incPass;
	static String username;
	static String loginInfo;

	/**
	 * Starts all the logic so we can use it anywhere.
	 */
	public static void startLogic() {
		startLog();
		getConfig();
		initializeBD();
		checkVersions();
	}

	/**
	 * Read the configuration file so we can use it later
	 */
	private static void getConfig() {
		try {
			fr = new FileReader(System.getProperty("user.dir") + "/src/files/config.conf");
			br = new BufferedReader(fr);

			String res = "";
			String[] config;

			hm = new HashMap<String, String>();
			// Separates the lines for the symbol. Links the rigth side with the left side
			while ((res = br.readLine()) != null) {

				config = res.split("=");
				hm.put(config[0], config[1]);
			}
		} catch (IOException ioe) {
			logger.warning("Can't acces to configuration file");
		}
	}

	/**
	 * Initialize the DataBase
	 */
	private static void initializeBD() {
		try {
			// Uses the configuration file to connect to the database
			con = DriverManager.getConnection("jdbc:mysql://" + hm.get("ip") + ":" + hm.get("port") + "/" + hm.get("bd")
					+ "?autoReconnectForPools=true", hm.get("user"), hm.get("passwd"));
			// Also creates a statement for later
			stmt = con.createStatement();
		} catch (Exception e) {
			logger.severe("Error: Can't connect to database");
			e.printStackTrace();
		}
	}

	/**
	 * Check if the client version is up-to-date
	 */
	private static void checkVersions() {
		try {

			ResultSet getVersion = stmt.executeQuery("select vers from server limit 1");

			getVersion.next();
			if (!clientVersion.equals(getVersion.getString(1))) {
				logger.severe("The client version isn't up-to-date");
				System.exit(-1);
			}
		} catch (SQLException e) {
			logger.warning("Can't get version from the server\n" + e.getMessage());
		}
	}

	/**
	 * Start the Log file (all the info will go there)
	 */
	private static void startLog() {
		try {
			// Creates a folder for storing the log if doesn't exists
			Path logPath = Paths.get(System.getProperty("user.dir") + "/src");
			if(Files.notExists(logPath)) {
				Files.createDirectory(logPath);
			}
			Path logPath2 = Paths.get(logPath+"/logs");
			if(Files.notExists(logPath2)) {
				Files.createDirectory(logPath);
			}
			handler = new FileHandler(System.getProperty("user.dir") + "/src/logs/default.log");
			logger = Logger.getLogger("breadmakesense");
			logger.addHandler(handler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tries to see if the username and password are in the database and if there is
	 * no user, creates one. Used to Log in
	 * 
	 * @param u The username
	 * @param p The password
	 */
	public static void attempLogin(String u, String p) {

		username = u;
		boolean usFound = false, end = false;
		// This while is in case the user have to be created it log in automatically
		while (!end) {
			try {
				getUname = stmt.executeQuery("select username from users");
				// Search for the username
				while (getUname.next()) {
					if (u.equals(getUname.getString(1))) {
						usFound = true;
						PreparedStatement pstmt = con.prepareStatement("select passwd from users where username = ?");
						pstmt.setString(1, u);

						ResultSet getPass = pstmt.executeQuery();
						// Checks if the password coincide
						if (getPass.next()) {
							if (p.equals(getPass.getString(1))) {
								logger.info("Connection established");
								incPass = false;
							} else {
								logger.warning("Error: Incorrect password");
								incPass = true;
							}
							end = true;
						}
					}
				}
				// If the user is not found it creates it
				if (!usFound) {
					PreparedStatement pstmt = con.prepareStatement("insert into users(username, passwd) values(?, ?)");
					pstmt.setString(1, u);
					pstmt.setString(2, p);

					pstmt.executeUpdate();
					logger.info("User created succesfully");
					end = true;
				}

			} catch (Exception e) {
				logger.severe("Error: Can't connect to database. " + e.getMessage());
				e.printStackTrace();
			}
		}
		loginInfo = (!usFound) ? "User created successfully" : "User logged successfully";
	}

	/**
	 * Gets the sum of all the breads generated by the users
	 * 
	 * @return The total of legacy_breads generated by the users
	 */
	public static double serverPuntuation() {
		try {
			ResultSet totalPunt = stmt.executeQuery("select sum(legacy_bread) from users");
			if (totalPunt.next()) {
				return totalPunt.getDouble(1);
			}
		} catch (SQLException e) {
			logger.info("Can't get total breads.\n" + e.getMessage());
		}
		return 0;

	}

	/**
	 * Checks if the username or the password comply the minium requeriments
	 * 
	 * @param u The username
	 * @param p The password
	 * @return True if the parameters are wrong. False if everything is ok.
	 */
	public static boolean loginInCheck(String u, String p) {
		if (u.trim().equals("") || u.contains(" ") || u.length() < 4
				|| !p.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{4,}$")) {
			return true;
		}
		return false;
	}

}
