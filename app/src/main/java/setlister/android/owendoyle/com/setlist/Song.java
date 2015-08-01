package setlister.android.owendoyle.com.setlist;

/**
 * Created by Owen on 31/07/2015.
 */
public class Song {
    String mTitle;
    String mArtist;

    public Song(String title, String artist){
        mTitle = title;
        mArtist = artist;
    }

    @Override
    public String toString() {
        return "Song title: "+mTitle+".   Song Artist: "+mArtist;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist;
    }
}
