import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private Library library;
    private static Connection connection;
    private static Statement statement;
    static Scanner s;
    private Integer user_id;
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

    public void menu() throws SQLException {
        System.out.println("---------------------------------Welcome to use Music App---------------------------------");
        System.out.println("1. Log in-----please input 1\n" +
                "2. Sign up-----please input 2");
        String ans = s.nextLine();
        if (ans.equals("1")) {
            this.logIn();
            this.afterLoginOrSignup();
        }else if(ans.equals("2")) {
            this.signUp();
            this.afterLoginOrSignup();
        }else{
            System.out.println("wrong input");
            this.menu();
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

    public void logIn() throws SQLException {
        System.out.println("-----Please input your user name: ");
        String name = s.nextLine();
        System.out.println("-----Please input your password: ");
        String pswd = s.nextLine();
        Boolean passwordCorrect = checkUserPassword(name,pswd);
        if (passwordCorrect) {
            ResultSet rs = statement.executeQuery("select user_id from users where user_name = '" + name +"'");
            user_id = rs.getInt("user_id");
        }else{
            System.out.println(
                    "1. Retry-----please input 1\n" +
                    "2. Sign up-----please input 2\n"+
                    "3. Exit-----please input 3\n"
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
                    this.menu();
                    break;
            }
        }

    }

    public void signUp() throws SQLException {
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
            String nextLine = s.nextLine();
            if ("1".equals(nextLine)) {
                this.signUp();
            } else if ("2".equals(nextLine)) {
                this.logIn();
            } else if ("3".equals(nextLine)) {
                this.menu();
            }else {
                System.out.println("wrong input");
                this.menu();
            }
        }
        // insert a new user into users
        if (!userNameExist) {
            statement.executeUpdate("insert into users (user_name, password) values('" +user_name+"', '"+pasw+"')");
            System.out.println("----------------------Congratulations! you signed up successfully.----------------------");
            ResultSet rs = statement.executeQuery("select user_id from users where user_name = '" + user_name +"'");
            user_id = rs.getInt("user_id");
        }
    }

    public void afterLoginOrSignup() throws SQLException {
        System.out.println(
                "1. Search a song-----please input 1\n" +
                "2. Search an artist-----please input 2\n"+
                "3. Search an album-----please input 3\n"+
                "4. Show playlists you made-----please input 4\n"+
                "5. Make a new playlist-----please input 5\n"+
                "6. Exit-----please input 6"
        );
        String ans = s.nextLine();
        if ("1".equals(ans)) {//Search a song
            Song song = searchSong();
            String s = song.toString();
            System.out.println(s);
        } else if ("2".equals(ans)) {//Search an artist
            Artist artist = searchArtist();
            String a = artist.toString();
            System.out.println(a);
        } else if ("3".equals(ans)) {//Search an album
            Album album = searchAlbum();
            String al = album.toString();
            System.out.println(al);
        } else if ("4".equals(ans)) {//show playlists you made
            showPlaylists();
        } else if ("5".equals(ans)) {//Make a new playlist
            Playlist playlist = makePlaylist();
            playlist.toSQL();
            String res = playlist.toString();
            System.out.println(res);
        } else if ("6".equals(ans)) {//Menu
            this.menu();
        } else {
            System.out.println("wrong input");
            this.menu();
        }

        this.afterLoginOrSignup();

    }

    //search a song in db or musicBrainz.
    public Song searchSong() {
        System.out.println("-----Please input song title: ");
        String songTitle = s.nextLine().toLowerCase();

        try {
            Song song = new Song(songTitle);
            ResultSet res = statement.executeQuery("select * from songs where name = '" + songTitle+"'");
            if (res.next()) {//song in db
                song.fromSQL(res);
                res = statement.executeQuery("select name from artists where artist_id = " + song.performer.dbID);
                song.performer.name = res.getString("name");
                res = statement.executeQuery("select name from albums where album_id = " + song.album.dbID);
                song.album.name = res.getString("name");
            } else {//fetch song from musicBrainz.
                String url = song.createURL(songTitle);
                Document doc = MusicBrainz(url);
                song.fromXML(doc);
                String query1 = song.performer.toSQL();
                statement.executeUpdate(query1);
                ResultSet r = statement.executeQuery("select * from artists where name = '"+song.performer.name+"'");
                song.performer.dbID = r.getInt("artist_id");
                String query2 = song.toSQL();
                statement.executeUpdate(query2);//store song in db
            }
            library.addSong(song);
            return song;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //search an artist in db or musicBrainz.
    public Artist searchArtist() {
        System.out.println("-----Please input artist name: ");
        String artistName = s.nextLine().toLowerCase();
        try {
            Artist artist = new Artist(artistName);
            ResultSet res = statement.executeQuery("select * from artists where name = '" + artistName+"'");
            if (res.next()) {//artist in db
                artist.fromSQL(res);
                res = statement.executeQuery("select * from songs where artist_id = " + artist.dbID);
                if (res.next()) {
                    artist.songs = (ArrayList<Song>) res.getArray("name");
                }

                res = statement.executeQuery("select * from albums where artist_id = " + artist.dbID);
                if (res.next()){
                    artist.albums = (ArrayList<Album>) res.getArray("name");
                }
            } else {//fetch song from musicBrainz.
                String url = artist.createURL(artistName);
                Document doc = MusicBrainz(url);
                artist.fromXML(doc);
                String query = artist.toSQL();
                statement.executeUpdate(query);//store artist in db
            }
            return artist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //search an album in db or musicBrainz.
    public Album searchAlbum(){
        System.out.println("-----Please input album name: ");
        String albumName = s.nextLine().toLowerCase();
        try {
            Album album = new Album(albumName);
            ResultSet res = statement.executeQuery("select * from songs where name = '" + albumName+"'");
            if (res.next()) {
                album.fromSQL(res);
                res = statement.executeQuery("select * from artists where artist_id = " + album.artist.dbID);
                if (res.next()) {
                    album.artist.name = res.getString("name");
                    album.artist.dbID =res.getInt("artist_id");
                }
                res = statement.executeQuery("select * from songs where album_id = " + album.dbID);
                if (res.next()) {
                    album.songs = (ArrayList<Song>) res.getArray("name");
                }
            } else {//fetch from musicBrainz.
                String url = album.createURL(albumName);
                Document doc = MusicBrainz(url);
                album.fromXML(doc);
                String query = album.toSQL();
                statement.executeUpdate(query);//store album in db
            }
            return album;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showPlaylists() throws SQLException {
        ResultSet res = statement.executeQuery("select * from playlists where user_id = " + this.user_id);
        String name = res.getString("name");
        System.out.println(name);

    }

    public Playlist makePlaylist() throws SQLException {
        System.out.println("-----Please input your new playlist name: ");
        String playlistName = s.nextLine();
        Playlist playlist = new Playlist(user_id);
        playlist.setName(playlistName);
        while (true) {
            Song cur = searchSong();
            playlist.addSong(cur);
            System.out.println("Added successfully!");
            System.out.println(
                    "1. add more songs-----please input 1\n" +
                    "2. Exit-----please input 2");
            String answer = s.nextLine();
            if ("1".equals(answer)) {
                continue;
            } else if ("2".equals(answer)) {
                break;
            }else {
                System.out.println("wrong input");
                this.menu();
            }
        }
        return  playlist;
    }

    //give a URL , return a document
    public static Document MusicBrainz(String initialURL) {
        /* now let's parse the XML.  */
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            URLConnection u = new URL(initialURL).openConnection();
            /* MusicBrainz asks to have a user agent string set. This way they can contact you if threre's an
             * issue, and they won't block your IP. */
            u.setRequestProperty("User-Agent", "Application ExampleParser/1.0 (cbrooks@usfca.edu");

            Document doc = db.parse(u.getInputStream());
            /* let's get the artist-list node */
            return doc;

        } catch (Exception ex) {
            System.out.println("XML parsing error" + ex);
        }

        return null;
    }


    public static void main(String[] args) throws SQLException {
        App app = new App();
        app.library = new Library();
        app.setupSQLite();
        s = new Scanner(System.in);

        //open the menu
        app.menu();
    }
}