import java.util.*;
import java.io.*;

/******************************************************************************
 * Example of HashSet and TreeSet.
 *
 * Implements a HashSet to remove duplicates but loses sorting, and TreeSet,
 * to remove duplicates keeping sorting. 
 *
 * To make possible to say that two instances of the class Song are equal,
 * the class must override the equals() and hashCode() methods.
 *
 * See also JukeboxArrayList.
 *
 */

public class JukeboxHashSet {
	ArrayList<Song> songList = new ArrayList<Song>();
	
	public static void main(String[] args) {
		new JukeboxHashSet().go();
	}
	public void go() {
		getSongs();
		
		// unsorted song list - order they appear in the text file
		System.out.println(songList);
		
		// sorting using the Comparable interface of the Song class
		Collections.sort(songList);
		System.out.println(songList);
		
		// removing duplicates using a HashSet
		// ... but loses sorting...
		HashSet<Song> songSet = new HashSet<Song>();
		songSet.addAll(songList);
		System.out.println(songSet);
		
		// and finally, remove duplicates and keep the sorting by using a TreeSet
		TreeSet<Song> songSet2 = new TreeSet<Song>();
		songSet2.addAll(songList);
		System.out.println(songSet2);
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
	
	// To make a HashSet that can recognize that 2 songs are the same,
	// the methods equals() and hashCode() must be overriden.
	// This is implemented here by using the String's methods
	// in the title instance variable.
	
	public boolean equals(Object aSong) { // Object's method uses (Object obj) parameter
		Song s = (Song) aSong;
		return getTitle().equals(s.getTitle());
	}
	public int hashCode() {
		return title.hashCode();
	}
}