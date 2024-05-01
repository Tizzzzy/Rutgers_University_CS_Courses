/*
 * Author: Xiaoyu Chen, Dong Shu
 * 
 */

package view;

public class SongName implements Comparable<SongName>{
	public SongName fullName;
	public String name;
	public String artist;
	public String album;
	public String year;
	
	public SongName(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public SongName() {}
	
	public static String getName(SongName songname) {
		String result = songname.name + "," + songname.artist;
		if(songname.album != null) {
			result += ("," + songname.album);
		}
		
		if(songname.year != null) {
			result += ("," + songname.year);
		}
		return result;
	}
	public static String getHalf(SongName songname) {
		String result = songname.name + "," + songname.artist;
		return result;
	}
	
	public int compareTo(SongName songname){
		int name = this.name.compareTo(songname.name);
		return name == 0 ? this.artist.compareTo(songname.artist) : name;
	}
	
	public static boolean checkEmpty(SongName songname) {
		boolean empty = false;
		
		if (songname.name == null || songname.artist == null) empty = true;
		
		
		return empty;
	}
}