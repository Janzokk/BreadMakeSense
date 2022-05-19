package p1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * methods
 */
public class methods {

	static Connection con;
	static Statement stmt;
	static FileWriter fw;
	static FileReader fr;
	static BufferedReader br;
	static HashMap<String, String> hm;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		initializeBD();

		long globalBreads = serverPuntuation();

		try {
			fw = new FileWriter("/media/jpg/5BCC-1467/sintesis/log.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("Username: ");
		String uname = in.nextLine();
		System.out.print("Password: ");
		String password = in.nextLine();

		attempLogin(uname, password);
	}

	public static void initializeBD() {
		try {
			fr = new FileReader("/media/jpg/5BCC-1467/sintesis/config.conf");
			br = new BufferedReader(fr);

			String res = "";
			String[] config;

			hm = new HashMap<String, String>();

			while ((res = br.readLine()) != null) {

				config = res.split("=");
				hm.put(config[0], config[1]);
			}

			con = DriverManager.getConnection(
					"jdbc:mysql://" + hm.get("ip") + ":" + hm.get("port") + "/" + hm.get("bd"), hm.get("user"),
					hm.get("passwd"));
			stmt = con.createStatement();
		} catch (Exception e) {
			try {
				fw.write("Error: Can't connect to database");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public static void attempLogin(String u, String p) {

		boolean usFound = false;

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
							fw.write("Connection established");
							System.out.println("Conected");
						} else {
							fw.write("Error: Incorrect password");
						}
					}
				}
			}
			if (!usFound) {
				PreparedStatement pstmt = con.prepareStatement("insert into users(username, passwd) values(?, ?)");
				pstmt.setString(1, u);
				pstmt.setString(2, p);

				int newUser = pstmt.executeUpdate();

			}

		} catch (Exception e) {
			try {
				fw.write("Error: Can't connect to database");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public static long serverPuntuation() {
		try {
			ResultSet totalPunt = stmt.executeQuery("select breads from server limit 1");
			if (totalPunt.next()) {
				return totalPunt.getLong(1);
			}
		} catch (SQLException e) {
			System.err.println("Can't get total breads");
			e.printStackTrace();
		}
		return 0;

	}

}