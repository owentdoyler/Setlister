package setlister.android.owendoyle.com.setlist;

import java.util.ArrayList;

/**
 * Created by Owen on 01/08/2015.
 */
public class Setlist {
    private ArrayList<Song> mSetlist;

    public Setlist(){
        mSetlist = new ArrayList<Song>();
    }

    public void addSong(Song song){
        mSetlist.add(song);
    }

    public void addSet(Set set){
        mSetlist.addAll(set.getSet());
    }

    public ArrayList<Song> getSetlist() {
        return mSetlist;
    }

    @Override
    public String toString() {
        String setlistString = "SETLIST: \n";
        for (Song song : mSetlist){
            setlistString += "\t"+song.toString() +"\n";
        }
        return setlistString;
    }
}
