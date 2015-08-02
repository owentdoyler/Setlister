package setlister.android.owendoyle.com.music;

/**
 * Created by Owen on 30/07/2015.
 */
public class Artist {
    private String mName;
    private String mMbid;
    private String mDisambiguation;

    public Artist(String name, String mbid){
        mName = name;
        mMbid = mbid;
    }

    public String getDisambiguation() {
        return mDisambiguation;
    }

    public void setDisambiguation(String Disambiguation) {
        this.mDisambiguation = Disambiguation;
    }

    public String getName() {
        return mName;
    }

    public String getMbid() {
        return mMbid;
    }
}
