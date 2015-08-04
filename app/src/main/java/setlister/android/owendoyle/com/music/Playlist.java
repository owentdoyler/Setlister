package setlister.android.owendoyle.com.music;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

/**
 * Created by Owen on 02/08/2015.
 */
public class Playlist {
    private String mArtist;
    private ArrayList<Song> mSongs;
    private String mPlaylistName;

    public Playlist(String artist){
        mSongs = new ArrayList<Song>();
        mArtist = artist;
    }

    public String getPlaylistName(){
        return mPlaylistName;
    }

    public boolean containsSong(String songTitle){
        for (Song song : mSongs){
            if (song.getTitle().toLowerCase().equals(songTitle.toLowerCase())){
                return true;
            }
        }
        return false;
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

    public String getArtist(){
        Song song = mSongs.get(0);
        return song.getArtist();
    }

    public ArrayList<Song> getSongs(){
        return mSongs;
    }

    public String[] getArtistsVariations(){
        String[] variations = {
                mArtist,
                mArtist.toLowerCase(),
                WordUtils.capitalize(mArtist),
                mArtist.toUpperCase()
        };
        return variations;
    }
}
