package setlister.android.owendoyle.com.setlist;

import java.util.ArrayList;

/**
 * Created by Owen on 01/08/2015.
 */
public class Setlists {
    private ArrayList<Setlist> mSetlists;

    public Setlists(){
        mSetlists = new ArrayList<Setlist>();
    }

    public void addSetlist(Setlist setlist){
        mSetlists.add(setlist);
    }

    public ArrayList<Setlist> getSetlists() {
        return mSetlists;
    }

    @Override
    public String toString() {
        String setlistsString = "SETLISTS \nGOT "+mSetlists.size()+"SETLISTS\n";
        for (Setlist set : mSetlists){
            setlistsString += "\t"+set.toString()+"\n";
        }
        return setlistsString;
    }
}
