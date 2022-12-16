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
    static Scanner s;
    //create a database and 6 tables. Store some data in it.
    public static void setupSQLite() throws SQLException {
        connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            //create 6 tables
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
            statement.executeUpdate("insert into users (user_name, password) values('Guo Fan','guofan1996')");
            statement.executeUpdate("insert into users (user_name, password) values('Yiyang Zhang','yiyang1997')");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        //check if username already existed.
    public static Boolean checkUserName(String name) {
        try {
            ResultSet res = statement.executeQuery("select password from users where user_name = '" + name+"'");
            if (res.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //check if the username and password are correct
    public static Boolean checkUserPassword(String name, String password) throws SQLException {

            ResultSet res = statement.executeQuery("select password from users where user_name = '" + name+"'");
            if (res.next()) {
                String realPassword = res.getString("password");
                if (realPassword.equals(password)) {
                    System.out.println("-----Welcome back! "+name);
                    return true;
                } else {
                    System.out.println("-----Your password is incorrect.");
                    return false;
                }
            }else {
                System.out.println("-----This username doesn't exist.");
                return false;
            }

    }

    public static void logIn() throws SQLException {
        System.out.println("-----Please input your user name: ");
        String name = s.nextLine();
        System.out.println("-----Please input your password: ");
        String pswd = s.nextLine();
        Boolean passwordCorrect = checkUserPassword(name,pswd);
       if (!passwordCorrect){
            System.out.println(
                    "1. Retry-----please input 1\n" +
                    "2. Sign up-----please input 2\n"+
                    "3. Exit-----please input 3"
            );
            String ans = s.nextLine();
            switch (ans) {
                case "1":
                    logIn();
                    break;
                case "2":
                    signUp();
                    break;
                case "3":
                    s.close();
                    break;
            }
        }
    }

    public static void signUp() throws SQLException {
        System.out.println("-----Please input your user name: ");
        String user_name = s.nextLine();
        System.out.println("-----Please input your password: ");
        String pasw = s.nextLine();
        Boolean userNameExist = checkUserName(user_name);

        if (userNameExist) {
            System.out.println("-----This user name has already existed.");
            System.out.println(
                    "1. Retry-----please input 1\n" +
                    "2. Log in-----please input 2\n"+
                    "3. Exit-----please input 3"
            );
            switch (s.nextLine()) {
                case "1":
                    signUp();
                    break;
                case "2":
                    logIn();
                    break;
                case "3":
                    s.close();
                    break;
            }
        }
        // insert a new user into users
        if (!userNameExist) {
            statement.executeUpdate("insert into users (user_name, password) values('" +user_name+"', '"+pasw+"')");
            System.out.println("----------------------Congratulations! you signed up successfully.----------------------");
        }
    }

    public void afterLoginOrSignup(){
        System.out.println(
                "1. Search a song-----please input 1\n" +
                "2. Search an album-----please input 2\n"+
                "3. Search an artist-----please input 3\n"+
                "4. Show playlists-----please input 4\n"+
                "5. Make a new playlist-----please input 5\n"+
                "6. Exit-----please input 6)"
        );
        String ans = s.nextLine();
        switch(ans) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
                s.close();
                break;
        }
    }

    //search a song in db, if it doesn't exist, fetch info from musicBrainz.


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
        app.setupSQLite();

        s = new Scanner(System.in);

        //log in or sign up
        System.out.println("---------------------------------Welcome to use Music App---------------------------------");
        System.out.println("1. Log in-----please input 1\n" +
                "2. Sign up-----please input 2");
        String ans = s.nextLine();
        if (ans.equals("1")) {
            app.logIn();
            app.afterLoginOrSignup();
        }else if(ans.equals("2")) {
            app.signUp();
            app.afterLoginOrSignup();
        }else{
            System.out.println("wrong input");
            s.close();
        }

    }
}
