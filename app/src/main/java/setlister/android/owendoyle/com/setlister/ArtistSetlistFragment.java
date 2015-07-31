package setlister.android.owendoyle.com.setlister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Owen on 31/07/2015.
 */
public class ArtistSetlistFragment extends Fragment {
    public static final String EXTRA_ARTIST_NAME = "com.owendoyle.android.setlister.artistSetlist.artist_name";
    public static final String EXTRA_ARTIST_MBID = "com.owendoyle.android.setlister.artistSetlist.artist_mbid";

    private static final String TAG = "ArtistSetlistFragment";

    private String mArtistName;
    private String mArtistMbid;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String artistName;
        String mbid;

        if(savedInstanceState == null){
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null){
                artistName = null;
                mbid = null;
            }
            else {
                artistName = extras.getString(EXTRA_ARTIST_NAME);
                mbid = extras.getString(EXTRA_ARTIST_MBID);
            }
        }
        else {
            artistName = (String) savedInstanceState.getSerializable(EXTRA_ARTIST_NAME);
            mbid = (String) savedInstanceState.getSerializable(EXTRA_ARTIST_MBID);
        }
        mArtistName = artistName;
        mArtistMbid = mbid;

    }
}
