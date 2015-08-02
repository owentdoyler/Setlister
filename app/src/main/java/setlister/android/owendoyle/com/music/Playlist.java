package setlister.android.owendoyle.com.music;

import java.util.ArrayList;

/**
 * Created by Owen on 02/08/2015.
 */
public class Playlist {
    private ArrayList<Song> mSongs;
    private String mPlaylistName;

    public Playlist(){
        mSongs = new ArrayList<Song>();
    }

    public String getPlaylistName(){
        return mPlaylistName;
    }

    public void setPlaylistName(String name){
        mPlaylistName = name;
    }

    public void addSongs(ArrayList<Song> songs){
        mSongs.addAll(songs);
    }

    public void addSong(Song song){
        mSongs.add(song);
    }

    public ArrayList<Song> getSongs(){
        return mSongs;
    }
}
