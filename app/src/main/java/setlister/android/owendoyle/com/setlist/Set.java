package setlister.android.owendoyle.com.setlist;

import java.util.ArrayList;

/**
 * Created by Owen on 01/08/2015.
 */
public class Set {
    private ArrayList<Song> mSet;
    private String mArtist;

    public Set(){
        mSet = new ArrayList<Song>();
    }

    public void addSong(Song song){
        mSet.add(song);
        mArtist = song.getArtist();
    }

    public String getArtist(){
        return mArtist;
    }

    public void addSongs(ArrayList<Song> songs){
        mSet.addAll(songs);
    }

    public ArrayList<Song> getSet() {
        return mSet;
    }
}
