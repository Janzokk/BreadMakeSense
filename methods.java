import java.sql.ResultSet;
import java.util.Scanner;
import java.security.cert.TrustAnchor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * methods
 */
public class methods {

    static Connection con;
    static Statement stmt;
    

    public static void main(String[] args) {
        initializeBD();
        
        FileSystem fs = FileSystems.getDefault();
            
        Path path = fs.getPath(".txt");
    }

    public static void initializeBD(){
        try {   
            con = DriverManager.getConnection("jdbc:mysql://192.168.2.11:3306/BMS", "jan", "1234");
            stmt = con.createStatement();
        } catch (Exception e) {
            System.err.println("Error: Can't connect to database");
            e.printStackTrace();
        }
    }

    public void attempLogin(String u, String p) {
        Scanner in = new Scanner(System.in);
        String uname = in.nextLine();
        String password = in.nextLine();
        boolean usFound = false;

        try {
            ResultSet getUname = stmt.executeQuery("select username from users");
            
            while (getUname.next()){

                if (uname == getUname.getString(1)){
                    usFound = true;
                    ResultSet getPass = stmt.executeQuery("select passwd from users where username = uname");
                    if(password == getPass.getString(1)){
                        
                    }else{
                        System.err.println("Incorrect password");
                    }
                }
            }
            if(!usFound){
                ResultSet newUser = stmt.executeQuery("insert into users(username, passwd) values(uname, password)");
            }
            
        } catch (Exception e) {
            System.out.println("Error: Can't connect to database");
            e.printStackTrace();
        }
    }

    public int serverPuntuation(){
        try {
            ResultSet totalPunt = stmt.executeQuery("select breads from server");
            return totalPunt.getInt(1);
        } catch (SQLException e) {
            System.err.println("Cant get total breads");
        }
       
    } 
    
}
