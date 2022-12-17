/**
 * Guo Fan - assignment 7
 * CS 514
 * My repo is GuoFan1996
 */
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Album extends Entity {
    protected ArrayList<Song> songs;
    protected Artist artist;

    public Album() {
        super();
        this.songs = new ArrayList<>();
        this.artist=new Artist();
    }

    public Album(String name) {
        super(name);
        this.songs = new ArrayList<>();
        this.artist=new Artist();
    }

    public String getName() {
        System.out.println("this is an album" + super.getName());
        return name;
    }

    public boolean equals(Album otherAlbum) {
        if ((this.artist.equals(otherAlbum.getArtist())) &&
                (this.name.equals(otherAlbum.getName()))) {
            return true;
        } else {
            return false;
        }
    }



    protected ArrayList<Song> getSongs() {
        return songs;
    }

    protected void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
    public String toSQL() {
        return "insert into albums (name, artist_id) values ('" + this.name + "', " + this.artist.dbID + ");";
    }

    public String toString() {
        String res = super.toString() +"\nartist: ";

        if (this.artist.equals("null")) {
            res = res+"Null";
        }else {
            res = res + this.artist.name+ " ";
        }
        res = res + "\nsongs: ";

        if (this.songs.isEmpty()) {
            res = res+"Null";
        }else {
            for (Song s: this.songs) {
                res = res + s.name +" ";
            }
        }
        return res;
    }

    public void fromSQL(ResultSet rs) {
        try {
            this.dbID = rs.getInt("album_id");
            this.name = rs.getString("name");
            this.artist.dbID=rs.getInt("artist");
        } catch(SQLException e) {
            System.out.println("SQL Exception" + e.getMessage());
        }

    }

    public String createURL(String albumName) {
        String url = "https://musicbrainz.org/ws/2/release?query="+albumName.strip().replaceAll(" ","%20")+"&fmt=xml";
        return url;
    }

    public void fromXML(Document document){
        NodeList artists = document.getElementsByTagName("artist-list");
        /* let's assume that the one we want is first. */
//        Node beatlesNode = artists.item(0).getFirstChild();
//        Node beatlesIDNode = beatlesNode.getAttributes().getNamedItem("id");
//        String id = beatlesIDNode.getNodeValue();
//
//        System.out.println(id);
    }
}

