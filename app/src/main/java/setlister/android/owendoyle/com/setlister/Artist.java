package setlister.android.owendoyle.com.setlister;

/**
 * Created by Owen on 30/07/2015.
 */
public class Artist {
    private String mName;
    private String mMbid;

    public Artist(String name, String mbid){
        mName = name;
        mMbid = mbid;
    }

    public String getName() {
        return mName;
    }

    public String getMbid() {
        return mMbid;
    }
}
