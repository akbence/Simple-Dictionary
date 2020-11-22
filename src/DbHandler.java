import model.DictionaryRow;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DbHandler {
    public static  Connection conn;
    private static DbHandler dbHandler;
    private DbHandler() {

    }
    public static DbHandler getDbHandler(){
        if(dbHandler == null){
            dbHandler = new DbHandler();
        }
        return dbHandler;
    }

    public static Connection connect() {
        conn = null;
        try {
            // db parameters
            // create a connection to the database
            Path currentRelativePath = Paths.get("");
            String urlVariable = currentRelativePath.toAbsolutePath().toString();
            String backslashChanged= urlVariable.replaceAll("\\\\", "/");
            String url = "jdbc:sqlite:"+ backslashChanged +  "/resources/sqlite.db";
            System.out.println("PATH :  "+ url);
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void disConnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void persistWord(DictionaryRow row){
        String sql = "INSERT INTO word(word,pronunciation,meaning,source,dateOfAdded) VALUES(?,?,?,?,?)";

        if( conn == null){
            connect();
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, row.getWord());
            pstmt.setString(2, row.getPronunciation());
            pstmt.setString(3, row.getMeaning());
            pstmt.setString(4, row.getSource());
            pstmt.setTimestamp(5, Timestamp.valueOf(row.getDateOfAdded()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();

    }


}
