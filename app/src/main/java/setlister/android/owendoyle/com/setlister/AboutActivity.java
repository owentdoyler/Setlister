package setlister.android.owendoyle.com.setlister;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

/**
 * Created by Owen on 02/09/2015.
 */
public class AboutActivity extends SingleFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_ic_arrow_back_48px_128);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarTitle = (TextView) findViewById(R.id.actionbar_title);
    }

    @Override
    protected Fragment createFragment() {
        return new AboutFragment();
    }
}
