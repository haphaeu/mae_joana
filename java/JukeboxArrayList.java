import java.util.*;
import java.io.*;

/******************************************************************************
 * Example of collections, comparable and comparator.
 *
 * Implements sorting of ArrayList by using:
 *
 * - A class implementing the Comparable interface and
 *
 * - A Comparator parameter to the Collections.sort() method.
 *
 */

public class JukeboxArrayList {
	ArrayList<Song> songList = new ArrayList<Song>();
	
	public static void main(String[] args) {
		new JukeboxArrayList().go();
	}
	public void go() {
		getSongs();
		
		// unsorted song list - order they appear in the text file
		System.out.println(songList);
		
		// sorting using the Comparable interface of the Song class
		Collections.sort(songList);
		System.out.println(songList);
		
		// sorting using a Comparator
		ArtistCompare artistCompare = new ArtistCompare();
		Collections.sort(songList, artistCompare);
		System.out.println(songList);
	}
	void getSongs() {
	    try {
		    File file = new File("SongList.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line=reader.readLine()) != null) {
			    addSong(line);
			}
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	void addSong(String lineToParse) {
	    String[] tokens = lineToParse.split("/");
		Song nextSong = new Song(tokens[0], tokens[1]);
		songList.add(nextSong);
	}
	
	// This inner class implements a Comparator interface responsible
	// to tell the Collections.sort() method how to compare two songs.
	// In this case, the songs are compared by artist.
	class ArtistCompare implements Comparator<Song> {
		public int compare(Song s1, Song s2) {
			return s1.getArtist().compareTo(s2.getArtist());
		}
	}
}

class Song implements Comparable<Song> {
	private String title;
	private String artist;
	
	Song (String a, String t) {
		title = t;
		artist = a;
	}
	
	public String getTitle() {
		return title;
	}
	public String getArtist() {
		return artist;
	}
	
	// override toString() from Object superclass.
	// when println(aSongList) is called, it calls the
	// toString() method for each element on the list.
	public String toString() {
		return title;
	}
	
	// implements Comparable<Song>
	// this enables instances of the class Song to be
	// comparable against each other in  sequential order,
	// in this case, alphabetically by title.
	public int compareTo(Song s) {
		return title.compareTo(s.getTitle());
	}
}