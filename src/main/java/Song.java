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

public class Song extends Entity implements Comparable<Song> {
    protected Album album;
    protected Artist performer;
    protected SongInterval duration;
    protected String genre;
    protected int likes;
    protected int BPM;
    protected Boolean hasBeenPlayed;
    protected String mood;

    public Song() {
        super();
        this.album=new Album();
        this.performer=new Artist();
    }

    public Song(String name) {
        super(name);
        this.album=new Album();
        this.performer=new Artist();
    }

    public Song(String name, int likes, int BPM, String genre, String mood, Boolean hasBeenPlayed) {
        super(name);
        this.likes = likes;
        this.BPM = BPM;
        this.genre = genre;
        this.mood = mood;
        this.hasBeenPlayed = hasBeenPlayed;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getBPM() {
        return BPM;
    }

    public void setBPM(int BPM) {
        this.BPM = BPM;
    }

    public Boolean getHasBeenPlayed() {
        return hasBeenPlayed;
    }

    public void setHasBeenPlayed(Boolean hasBeenPlayed) {
        this.hasBeenPlayed = hasBeenPlayed;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setLength(int length) {
        duration = new SongInterval(length);
    }

    public String showLength() {
        return duration.toString();
    }


    protected Album getAlbum() {
        return album;
    }

    protected void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getPerformer() {
        return performer;
    }

    public void setPerformer(Artist performer) {
        this.performer = performer;
    }

    public boolean defiEqual(Song other){
        return  (this.name.equals(other.name)) &&
                (this.album.name.equals(other.album.name)) &&
                (this.performer.name.equals(other.performer.name));
    }

    public boolean posbEqual(Song other){
        Boolean isPossiblyDup1 = this.name.equals(other.name) &&
                (this.performer.name.equals(other.performer.name) || this.album.name.equals(other.album.name));

        String thisname = trimPun(this.name);
        String othername = trimPun(other.name);
        Boolean isPossiblyDup2 = this.performer.name.equals(other.performer.name) &&
                this.album.name.equals(other.album.name) &&
                thisname.equals(othername);

        return isPossiblyDup1 || isPossiblyDup2;
    }

    public String trimPun(String songName) {
        while (!songName.isBlank() &&
                !songName.substring(songName.length()-1,songName.length()).matches("[A-Za-z0-9]")) {
            songName = songName.substring(0,songName.length()-1);
        }
        return songName.toLowerCase();
    }

    public String toString() {
        String res = super.toString() +"\nartist: ";

        if (this.performer.name.equals("null")) {
            res = res+"Null";
        }else {
            res = res + this.performer.name;
        }
        res = res + "\nalbum: ";

        if (this.album.name.equals("null")) {
            res = res+"Null";
        }else {
            res = res + this.album.name;
        }
        return res;
    }

    public String toXML(){
        return "<song>\n<title>" + name + "</title>\n<artist>"+ performer.getName()
                + "</artist>\n<album>"+ album.getName() +"</album>\n</song>";
    }

    //parse XML to find musicBrainzeID and artist.
    public void fromXML(Document document){
        Node recording = document.getElementsByTagName("recording").item(0);
        String recordingID = recording.getAttributes().getNamedItem("id").getNodeValue();
        this.musicBrainzeID = recordingID;
    }

    public int compareTo(Song other) {
        return Integer.compare(this.likes,other.likes);
    }

    public String toSQL() {
        String query = "insert into songs (name, artist_id, album_id) values ('" +
                this.name.toLowerCase() +
                "', " + performer.dbID +
                ", " +
                album.dbID
                + ");";
        return query;
    }

    public void fromSQL(ResultSet rs) {
        try {
            this.dbID = rs.getInt("song_id");
            this.name = rs.getString("name");
            this.performer.dbID = rs.getInt("artist");
            this.album.dbID = rs.getInt("album");
        } catch(SQLException e) {
            System.out.println("SQL Exception" + e.getMessage());
        }

    }

    public String createURL(String songTitle) {
        //https://musicbrainz.org/ws/2/recording?query=as%20long%20as%20you%20love%20me&fmt=xml
        String url = "https://musicbrainz.org/ws/2/recording?query="+songTitle.replaceAll(" ","%20")+"&fmt=xml";
        return url;
    }
}