import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Playlist {
    private List<Song> songList;

    public Playlist() {
        songList = new ArrayList<Song>();
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void addSong(Song s) {
        songList.add(s);
    }


    public void deleteSong(Song s) {
        if (songList.contains(s)) {
            songList.remove(s);
        }else {
            System.out.printf("%s not in the playlist\n",s.toString());
        }
    }

    public void merge(Playlist other) {
        Set<Song> s1 = new HashSet<>(this.songList);
        Set<Song> s2 = new HashSet<>(other.songList);
        s1.addAll(s2);
        this.songList = new ArrayList<>(s1);
    }

    public void sortByLikes(){
        List<Song> sortedList = songList.stream()
                .sorted((x,y) -> (x.compareTo(y)))
                .collect(Collectors.toList());

        sortedList.sort(Collections.reverseOrder());

        songList = sortedList;
    }

    public void shuffle() {
        Collections.shuffle(songList);
    }

    public Playlist createPlaylistBy(String feature,String limit ) {
        Playlist returnList = new Playlist();
        switch (feature){
            case "likes":
                try {
                    int likes = Integer.parseInt(limit);
                    for (Song song : this.getSongList()) {
                        if ( song.getLikes() >= likes){
                            returnList.getSongList().add(song);
                        }
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("for feature likes, limit should be an integer");
                    throw new RuntimeException(e);
                }
            case "genre":
                for (Song song : this.getSongList()) {
                    if ( song.getGenre().equalsIgnoreCase(limit)){
                        returnList.getSongList().add(song);
                    }
                }
                break;
            case "BPM":
                try {
                    int BPM = Integer.parseInt(limit);
                    for (Song song : this.getSongList()) {
                        if ( song.getBPM() >= BPM){
                            returnList.getSongList().add(song);
                        }
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("for feature BPM, limit should be an integer");
                    throw new RuntimeException(e);
                }
            case "hasBeenPlayed":
                try {
                    Boolean hasBeenPlayed = Boolean.parseBoolean(limit);
                    for (Song song : this.getSongList()) {
                        if (song.getHasBeenPlayed() == hasBeenPlayed) {
                            returnList.getSongList().add(song);
                        }
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("for feature hasBeenPlayed, limit should be Ture or False");
                    throw new RuntimeException(e);
                }
            case "mood":
                for (Song song : this.getSongList()) {
                    if ( song.getMood().equalsIgnoreCase(limit)){
                        returnList.getSongList().add(song);
                    }
                }
                break;

        }
        return returnList;
    }

    public void playlistToXML() throws IOException {
        StringBuilder sb= new StringBuilder();

        sb.append("<songs>");
        for (Song song : songList) {
            sb.append("\n"+song.toXML());
        }
        sb.append("\n</songs>");
        String xmlSource = sb.toString();
        java.io.FileWriter fw = new java.io.FileWriter("playlist.xml");
        fw.write(xmlSource);
        fw.close();
    }

}
