import junit.framework.TestCase;
import org.w3c.dom.Document;

public class SongTest extends TestCase {

    public void testCreateURL() {
        Song song = new Song();
        song.createURL("as long as you love me");
    }


    public void testToXML() {
    }

    public void testFromXML() {
        App app = new App();
        Document doc = app.MusicBrainz("https://musicbrainz.org/ws/2/recording?query=Now%20you%20see%20me&fmt=xml");
        Song song = new Song("Now You See Me");
        song.fromXML(doc);
        String s = song.toString();
        System.out.println(s);

    }

    public void testToSQL() {
    }

    public void testFromSQL() {
    }
}