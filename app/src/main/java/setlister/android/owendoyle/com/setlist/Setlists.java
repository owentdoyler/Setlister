package setlister.android.owendoyle.com.setlist;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Owen on 01/08/2015.
 */
public class Setlists {

    private String mArtist;
    private ArrayList<Setlist> mSetlists;

    public Setlists(){
        mSetlists = new ArrayList<Setlist>();
    }

    public void addSetlist(Setlist setlist){
        mSetlists.add(setlist);
        mArtist = setlist.getArtist();
    }

    public ArrayList<Setlist> getSetlists() {
        return mSetlists;
    }

    @Override
    public String toString() {
        String setlistsString = "SETLISTS \nGOT "+mSetlists.size()+"SETLISTS\n";
        for (Setlist set : mSetlists){
            setlistsString += set.toString()+"\n";
        }
        return setlistsString;
    }

    public ArrayList<Song> getMostPlayed(){
        ArrayList<Song> songs = new ArrayList<Song>();
        HashMap<String, Integer> songCountMap = getCountMap();
        for (String songTitle : songCountMap.keySet()){
            int count = songCountMap.get(songTitle);
            if (count > ((mSetlists.size()/2 > 4)? mSetlists.size()/2 : 0)){
                songs.add(new Song(songTitle, mArtist));
            }
        }
        return songs;
    }


    private HashMap<String, Integer> getCountMap(){
        HashMap<String, Integer> songCountMap = new HashMap<String, Integer>();
        int count = 0;
        for (Setlist setlist : mSetlists){
            ArrayList<Song> songs = setlist.getSetlist();
            for (Song song : songs){
                String songTitle = song.getTitle();
                if (songCountMap.containsKey(songTitle)){
                    count = songCountMap.get(songTitle);
                    count += 1;
                    songCountMap.put(songTitle, count);
                }
                else {
                    songCountMap.put(songTitle, 1);
                }
            }
        }
        return songCountMap;
    }
}
