package setlister.android.owendoyle.com.setlist;

import java.util.ArrayList;

/**
 * Created by Owen on 01/08/2015.
 */
public class Set {
    private ArrayList<Song> mSet;

    public Set(){
        mSet = new ArrayList<Song>();
    }

    public void addSong(Song song){
        mSet.add(song);
    }

    public void addSongs(ArrayList<Song> songs){
        mSet.addAll(songs);
    }

    public ArrayList<Song> getSet() {
        return mSet;
    }
}
