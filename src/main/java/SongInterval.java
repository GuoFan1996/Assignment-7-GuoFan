/**
 * Guo Fan - assignment 7
 * CS 514
 * My repo is GuoFan1996
 */
public class SongInterval {
    private int length;

    public SongInterval(int len) {
        length = len;
    }

    public SongInterval() {
    }

    public String toString(){
        int minutes = length / 60 ;
        int seconds = length % 60 ;

        return String.format("%d:%d", minutes, seconds);
    }

}
