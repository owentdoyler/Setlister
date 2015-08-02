package setlister.android.owendoyle.com.music;

/**
 * Created by Owen on 31/07/2015.
 */
public class Song {
    private String mTitle;
    private String mArtist;
    private boolean mSelected;

    public Song(String title, String artist){
        mTitle = title;
        mArtist = artist;
        mSelected = true;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
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
