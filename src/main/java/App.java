import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.Scanner;

public class App {

    private Library library;
    private static Connection connection;
    private static Statement statement;
    //create a database and 6 tables. Store some data in it.
    public static void setupSQLite() {
        connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            // create 6 tables
            statement.executeUpdate("drop table if exists users");
            statement.executeUpdate("drop table if exists playlists");
            statement.executeUpdate("drop table if exists playlists_contents");
            statement.executeUpdate("drop table if exists songs");
            statement.executeUpdate("drop table if exists albums");
            statement.executeUpdate("drop table if exists artists");

            statement.executeUpdate("create table users (user_id integer primary key autoincrement, user_name string not null, password string not null)");
            statement.executeUpdate("create table playlists (playlist_id integer primary key autoincrement, name string not null, user_id integer)");
            statement.executeUpdate("create table playlists_contents (content_id integer primary key autoincrement, playlist_id integer not null, song_id integer not null)");
            statement.executeUpdate("create table songs (song_id integer primary key autoincrement, name string not null, artist_id integer not null, album_id integer)");
            statement.executeUpdate("create table albums (album_id integer primary key autoincrement, name string not null, artist_id integer not null)");
            statement.executeUpdate("create table artists (artist_id integer primary key autoincrement, name string not null)");

            //insert 2 users
            statement.executeUpdate("insert into users (user_name, password) values('Guo Fan')");
            statement.executeUpdate("insert into users (user_name, password) values('Yiyang Zhang')");

            //
//            statement.executeUpdate("insert into person values(2, 'yui')");

//            ResultSet rs = statement.executeQuery("select * from person");
//            while (rs.next()) {
//                // read the result set
//                System.out.println("name = " + rs.getString("name"));
//                System.out.println("id = " + rs.getInt("id"));
//
//            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public Boolean checkUserPassword(String name,String password) throws SQLException {
        try {
            ResultSet res = statement.executeQuery("select password from users where user_name = " + name);
            Array ans = res.getArray("password");
            String realPassword = ans.toString();
            if (realPassword.equals(password)) {
                System.out.println("Welcome back! "+name);
                return true;
            } else {
                System.out.println("Either User name or password is incorrect");
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //give a URL , return a document
    public static Document MusicBrainz(String initialURL) {
        //String URL ="https://musicbrainz.org/ws/2/artist?query=beatles&fmt=xml";
        /* MusicBrainz gives each element in their DB a unique ID, called an MBID. We'll use this to fecth that. */
        /* now let's parse the XML.  */
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            URLConnection u = new URL(initialURL).openConnection();
            /* MusicBrainz asks to have a user agent string set. This way they can contact you if there's an
             * issue, and they won't block your IP. */
            u.setRequestProperty("User-Agent", "Application ExampleParser/1.0 (gfan3@dons.usfca.edu");

            Document doc = db.parse(u.getInputStream());
            return doc;

        } catch (Exception ex) {
            System.out.println("XML parsing error" + ex);
            return null;
        }
    }

    public static void main(String[] args) throws SQLException {
        App app = new App();
        app.library=new Library();
        app.setupSQLite();


        Scanner s = new Scanner(System.in);
        System.out.println("---------------------------------Welcome to use Music App---------------------------------");
        System.out.println("1. Log in-----please input 1" +
                "/n" +
                "2. Sign up-----please input 2");
        switch (s.next()){
            case "1":
                System.out.println("-----Please input your user name: ");
                String user_name = s.next();
                System.out.println("-----Please input your password: ");
                String password = s.next();
                Boolean passwordCorrect = app.checkUserPassword(user_name,password);
                Boolean retry = true;
                while (!passwordCorrect && retry){

                }

            case "2":


        }

    }
}
