package setlister.android.owendoyle.com.setlister;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

/**
 * Created by Owen on 31/07/2015.
 */
public class ArtistSetlistActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ArtistSetlistFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_playlist_name_layout);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_ic_arrow_back_48px_128);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
