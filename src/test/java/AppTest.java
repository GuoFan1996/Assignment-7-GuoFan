import junit.framework.TestCase;

public class AppTest extends TestCase {

    public void testMusicBrainz() {
        App app =new App();
        app.MusicBrainz("https://musicbrainz.org/ws/2/recording?query=as%20long%20as%20you%20love%20me&fmt=xml");
    }
}