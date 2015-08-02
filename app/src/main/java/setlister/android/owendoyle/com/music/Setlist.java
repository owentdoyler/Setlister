package setlister.android.owendoyle.com.music;

import java.util.ArrayList;

/**
 * Created by Owen on 01/08/2015.
 */
public class Setlist {
    private ArrayList<Song> mSetlist;
    private String mArtist;

    public Setlist(){
        mSetlist = new ArrayList<Song>();
    }

    public void addSet(Set set){
        mSetlist.addAll(set.getSet());
        mArtist = set.getArtist();
    }

    public String getArtist(){
        return mArtist;
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
