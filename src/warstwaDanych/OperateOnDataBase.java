package warstwaDanych;

import java.sql.*;
import java.util.ArrayList;

/**
 * Operacje na bazie danych
 */
public class OperateOnDataBase {

    String dbURL = "jdbc:sqlserver://TWIERDZA\\ARTURROSERVER; databaseName = bazaUtworow";
    String user = "proba";
    String password = "proba1234";
    Connection con;
    Statement statement;
    ArrayList<Integer> listOfDeletedSongs = new ArrayList<>();
    int maxIndex = 0;

    /**
     * Zwrocenie ilości rekordów w bazie danych
     */
    public int countRows() throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM piosenki");
        int numberOfSongs = 0;
        while (result.next()){ numberOfSongs++; }
        return numberOfSongs;
    }
    /**
     * Zwrocenie lancucha znakowego z wszystkimi rekordami z bazy danych
     */
    public Object[][] selectAll() throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM piosenki");
        int numberOfSongs = countRows();
        Object[][] tab = new String[numberOfSongs+1][5];
        //tab[0] = new String[] {"Tytuł", "Wykonawca", "Album", "Długość", "ID"};
        tab[0][0] = "Tytuł";
        tab[0][1] = "               Wykonawca";
        tab[0][2] = "                     Album";
        tab[0][3] = "                         Długość";
        tab[0][4] = "   ID";
        for(int j = 0; j < numberOfSongs; j++){
            result.next();
            for (int i = 1; i < 6; i++) {
                if (i != 4)
                    tab[j + 1][i - 1] = (result.getString(i));
                else
                    tab[j + 1][i - 1] = (result.getString(i).substring(0, 8));
            }
        }
        return tab;
    }

    public void fromDBToListOfSongs(ListOfSongs list) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM piosenki");
        while(result.next()){
            Song tmp = new Song(result.getString(1).strip(), result.getString(2).strip(), result.getString(3).strip(), result.getTime(4), result.getInt(5));
            list.addSong(tmp);
        }
    }

    /**
     * Zwrocenie lancucha znakowego z pojedynczym rekordem z bazy danych
     */
    public String selectOne(int index) throws SQLException {
        boolean operationDone = false;
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM piosenki");
        StringBuilder tmp = new StringBuilder();
            while (result.next()) {
                if (result.getString("id").equals(Integer.toString(index))) {
                    operationDone = true;
                    for (int i = 1; i < 6; i++) {
                        if (i != 4)
                            tmp.append(result.getString(i).strip()).append(", ");
                        else
                            tmp.append("\n").append(result.getString(i).substring(0, 6)).append(Math.round(Float.parseFloat(result.getString(i).substring(6, 15)))).append(",");
                    }
                    tmp.append('\n');
                    break;
                }
            }
//        if (!operationDone)
//            throw new SQLException("Nie ma rekordu o takim indeksie");
        return tmp.toString();
    }

    public String getTitle(int index) throws SQLException {
        StringBuilder tmp = new StringBuilder();
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT Tytul FROM piosenki WHERE id = " + "'" + index + "'");
        while(result.next())
            tmp.append(result.getString("Tytul").strip());
        return tmp.toString();
    }

    /**
     * Wpisanie rekordu do bazy danych
     */
    public void insert(String title, String performer, String album, String duration, int index) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM piosenki");
        while(result.next())
        {
            if(result.getString("id").equals(Integer.toString(index)))
                throw new SQLException("Utwór o takim id już istnieje");
        }
        statement.executeUpdate("INSERT INTO piosenki VALUES(" +
                "'" + title + "'," + " " +
                "'" + performer + "'," + " " +
                "'" + album + "'," + " " +
                "'" + duration + "'," + " " +
                "'" + index + "'" + ")");
    }

    /**
     * Usuniecie rekordu z bazy danych
     */
    public void delete(int id) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        statement.executeUpdate("DELETE FROM piosenki WHERE id = " + "'" + id + "'");
        listOfDeletedSongs.add(id);
    }

    /**
     * Zaktualizowanie tytułu konkretnego utworu
     */
    public void updateTitle(int id, String newTitle) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        statement.executeUpdate("UPDATE piosenki SET Tytul = " + "'" + newTitle + "'" +
                "WHERE id = " + "'" + id + "'");
    }

    /**
     * Zaktualizowanie wykonawcy konkretnego utworu
     */
    public void updatePerformer(int id, String newPerformer) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        statement.executeUpdate("UPDATE piosenki SET Wykonawca = " + "'" + newPerformer + "'" +
                "WHERE id = " + "'" + id + "'");
    }

    /**
     * Zaktualizowanie albumu konkretnego utworu
     */
    public void updateAlbum(int id, String newAlbum) throws SQLException {
        con = DriverManager.getConnection(dbURL, user, password);
        statement = con.createStatement();
        statement.executeUpdate("UPDATE piosenki SET Album = " + "'" + newAlbum + "'" +
                "WHERE id = " + "'" + id + "'");
    }
}
