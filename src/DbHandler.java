import model.DictionaryRow;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            String url = "jdbc:sqlite:"+ backslashChanged +  "/sqlite.db";
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
        String sql = "INSERT INTO Dictionary(word,pronunciation,meaning,source,dateOfAdded) VALUES(?,?,?,?,?)";

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

    public List<DictionaryRow> getDictionaryRows(int limit, int offset, FilterType filterType, String filterWord){
        String sql = "SELECT word,pronunciation,meaning,source,dateOfAdded FROM Dictionary ORDER BY dateOfAdded DESC LIMIT ? OFFSET ?";
        if(filterType == FilterType.PRONOUNCIATION){
            sql = "SELECT word,pronunciation,meaning,source,dateOfAdded FROM Dictionary WHERE pronunciation=? ORDER BY dateOfAdded DESC LIMIT ? OFFSET ?";
        }
        if(filterType == FilterType.MEANING){
            sql = "SELECT word,pronunciation,meaning,source,dateOfAdded FROM Dictionary WHERE meaning=? ORDER BY dateOfAdded DESC LIMIT ? OFFSET ?";
        }
        if(filterType == FilterType.BOOKSOURCE){
            sql = "SELECT word,pronunciation,meaning,source,dateOfAdded FROM Dictionary WHERE source LIKE ? ORDER BY dateOfAdded DESC LIMIT ? OFFSET ?";
        }
        List<DictionaryRow> rows = new ArrayList<>();
        if( conn == null){
            connect();
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if(filterType == FilterType.NONE){
                pstmt.setInt(1 , limit);
                pstmt.setInt(2 , offset);
            }
            else if (filterType == FilterType.BOOKSOURCE){
                pstmt.setString(1 , filterWord + "%");
                pstmt.setInt(2 , limit);
                pstmt.setInt(3 , offset);
            }
            else {
                pstmt.setString(1 , filterWord);
                pstmt.setInt(2 , limit);
                pstmt.setInt(3 , offset);
            }
            System.out.println(sql);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                DictionaryRow row =  new DictionaryRow();
                row.setWord(resultSet.getString("word"));
                row.setPronunciation(resultSet.getString("pronunciation"));
                row.setMeaning(resultSet.getString("meaning"));
                row.setSource(resultSet.getString("source"));
                row.setDateOfAdded(resultSet.getTimestamp("dateOfAdded").toLocalDateTime());
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rows;
    }

    public int getFilteredDictionaryCount(FilterType filterType, String filterWord){
        String sql = "SELECT COUNT (*) FROM Dictionary";
        if(filterType == FilterType.PRONOUNCIATION){
            sql = "SELECT COUNT (*) FROM Dictionary WHERE pronunciation=? ";
        }
        if(filterType == FilterType.MEANING){
            sql = "SELECT COUNT (*) FROM Dictionary WHERE meaning=? ";
        }
        if(filterType == FilterType.BOOKSOURCE){
            sql = "SELECT COUNT (*) FROM Dictionary WHERE source LIKE ?";

        }
        int result = 0;
        if( conn == null){
            connect();
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (filterType == FilterType.BOOKSOURCE){
                pstmt.setString(1 , filterWord + "%");
            }
            if (filterType == FilterType.MEANING || filterType == FilterType.PRONOUNCIATION){
                pstmt.setString(1 , filterWord);
            }
            if (filterType != FilterType.NONE){
                pstmt.setString(1 , filterWord);
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                result =resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void deleteWord(String word) throws SQLException {
        String sql = "DELETE FROM Dictionary WHERE word=?";
        if( conn == null){
            connect();
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word);
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public DictionaryRow findDictRowByWord(String word){
        String sql = "SELECT word,pronunciation,meaning,source,dateOfAdded FROM Dictionary where word = ?";
        DictionaryRow row = null;
        if( conn == null){
            connect();
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                row = new DictionaryRow();
                row.setWord(resultSet.getString("word"));
                row.setPronunciation(resultSet.getString("pronunciation"));
                row.setMeaning(resultSet.getString("meaning"));
                row.setSource(resultSet.getString("source"));
                row.setDateOfAdded(resultSet.getTimestamp("dateOfAdded").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return row;
    }
}
