package setlister.android.owendoyle.com.setlister;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Owen on 02/09/2015.
 */
public class AboutFragment extends Fragment{

    private static final String SETLISTFM_URL= "http://www.setlist.fm/";
    private static final String MUSICBRAINZ_URL = "https://musicbrainz.org/";

    private ImageView mSetlistfmLogo;
    private ImageView mMusicbrainzLogo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        ((AboutActivity)getActivity()).setActionBarTitle("About Setlister");

        mSetlistfmLogo = (ImageView)v.findViewById(R.id.setlistfm_logo_image);
        mSetlistfmLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SETLISTFM_URL));
                startActivity(browserIntent);
            }
        });

        mMusicbrainzLogo = (ImageView) v.findViewById(R.id.musicbrainz_logo_image);
        mMusicbrainzLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MUSICBRAINZ_URL));
                startActivity(browserIntent);
            }
        });

        return v;
    }
}
