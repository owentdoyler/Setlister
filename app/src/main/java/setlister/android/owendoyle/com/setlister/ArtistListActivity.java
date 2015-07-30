package setlister.android.owendoyle.com.setlister;

import android.support.v4.app.Fragment;

/**
 * Created by Owen on 30/07/2015.
 */
public class ArtistListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ArtistListFragment();
    }
}
