/**
 * Guo Fan - assignment 7
 * CS 514
 * My repo is GuoFan1996
 */
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Artist extends Entity {

    protected ArrayList<Song> songs;
    protected ArrayList<Album> albums;

    public Artist(String name) {
        super(name);
        this.songs = new ArrayList<>();
        this.albums = new ArrayList<>();
    }

    public Artist() {
        super();
    }

    protected ArrayList<Song> getSongs() {
        return songs;
    }

    protected void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    protected ArrayList<Album> getAlbums() {
        return albums;
    }

    protected void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public void addSong(Song s) {
        songs.add(s);
    }

    public String toString() {
        String res = super.toString() +"\nalbums: ";

        if (this.albums.isEmpty()) {
            res = res+ "null";
        }else {
            for (Album a: this.albums) {
                res = res + a.name+ " ";
            }
        }

        res = res + "\nsongs: ";
        if (this.songs.isEmpty()) {
            res = res+ "null";
        }else {
            for (Song s: this.songs) {
                res = res + s.name + " ";
            }
        }
        return res;
    }

    public String toSQL() {
        return "insert into artists (name) values ('" + this.name + "');";
    }

    public void fromSQL(ResultSet rs) {
        try {
            this.dbID = rs.getInt("artist_id");
            this.name = rs.getString("name");
        } catch(SQLException e) {
            System.out.println("SQL Exception" + e.getMessage());
        }

    }
    public void fromXML(Document document){
        NodeList artists = document.getElementsByTagName("artist-list");

        //parse ID
        Node beatlesNode = artists.item(0).getFirstChild();
        Node beatlesIDNode = beatlesNode.getAttributes().getNamedItem("id");
        String id = beatlesIDNode.getNodeValue();
        this.musicBrainzeID = id;
    }

    public String createURL(String artistName) {
        String url = "https://musicbrainz.org/ws/2/artist?query="+artistName.strip().replaceAll(" ","%20")+"&fmt=xml";
        return url;
    }

}

