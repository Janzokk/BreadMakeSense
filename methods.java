import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * methods
 */
public class methods {

    public static void main(String[] args) {
        initializeBD();
    }

    public static void initializeBD(){
        try {   
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.2.11:3306/BMS", "jan", "1234");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select breads from server");
        } catch (Exception e) {
            System.err.println("Error: Can't connect to database");
            e.printStackTrace();
        }
    }

    public void attempLogin(String u, String p) {
    
    }
    
}
