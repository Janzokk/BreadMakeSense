package breadmakesense;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * methods
 */
public class LoginWindowLogic {

	static Connection con;
	static Statement stmt;
	static FileHandler handler;
	static Logger logger;
	static FileReader fr;
	static BufferedReader br;
	static HashMap<String, String> hm;

	static String clientVersion = "1.0";
	long globalBreads = serverPuntuation();
	static boolean incPass;
	static String username;

	public static void startLogic() {
		startLog();
		getConfig();
		initializeBD();
		checkVersions();
	}

	public static void getConfig() {
		try {
			fr = new FileReader(System.getProperty("user.dir") + "/src/files/config.conf");
			br = new BufferedReader(fr);

			String res = "";
			String[] config;

			hm = new HashMap<String, String>();

			while ((res = br.readLine()) != null) {

				config = res.split("=");
				hm.put(config[0], config[1]);
			}
		} catch (IOException ioe) {
			logger.warning("Can't acces to configuration file");
		}
	}

	public static void initializeBD() {
		try {

			con = DriverManager.getConnection(
					"jdbc:mysql://" + hm.get("ip") + ":" + hm.get("port") + "/" + hm.get("bd"), hm.get("user"),
					hm.get("passwd"));
			stmt = con.createStatement();
		} catch (Exception e) {
			logger.severe("Error: Can't connect to database");
			e.printStackTrace();
		}
	}

	public static void checkVersions() {
		try {

			ResultSet getVersion = stmt.executeQuery("select vers from server limit 1");

			getVersion.next();
			if (!clientVersion.equals(getVersion.getString(1))) {
				logger.severe("The client version isn't up-to-date");
				System.exit(-1);
			}
		} catch (SQLException e) {
			logger.warning("Can't get version from the server");
			e.printStackTrace();
		}
	}

	public static void startLog() {
		try {
			handler = new FileHandler(System.getProperty("user.dir") + "/src/files/default.log");
			logger = Logger.getLogger("p1");
			logger.addHandler(handler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void attempLogin(String u, String p) {
		
		username = u;
		boolean usFound = false, end = false;

		while (!end) {
			try {
				ResultSet getUname = stmt.executeQuery("select username from users");

				while (getUname.next()) {

					if (u.equals(getUname.getString(1))) {
						usFound = true;
						PreparedStatement pstmt = con.prepareStatement("select passwd from users where username = ?");
						pstmt.setString(1, u);

						ResultSet getPass = pstmt.executeQuery();
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
				if (!usFound) {
					PreparedStatement pstmt = con.prepareStatement("insert into users(username, passwd) values(?, ?)");
					pstmt.setString(1, u);
					pstmt.setString(2, p);

					pstmt.executeUpdate();
					logger.info("User created");
				}

			} catch (Exception e) {
				logger.severe("Error: Can't connect to database");
			}
		}
	}

	public static long serverPuntuation() {
		try {
			ResultSet totalPunt = stmt.executeQuery("select sum(legacy_bread) from users");
			if (totalPunt.next()) {
				return totalPunt.getLong(1);
			}
		} catch (SQLException e) {
			logger.info("Can't get total breads");
			e.printStackTrace();
		}
		return 0;

	}

	public static boolean loginInCheck(String u, String p) {
		if (u.trim().equals("") || p.trim().equals("") || u.contains(" ") || p.contains(" ")) {
			return true;
		} else {
			return false;
		}
	}
}
