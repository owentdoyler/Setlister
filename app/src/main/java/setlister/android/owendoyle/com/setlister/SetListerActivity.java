package setlister.android.owendoyle.com.setlister;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SetListerActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        mActionBarTitle = (TextView) findViewById(R.id.actionbar_title);
    }

    @Override
    protected Fragment createFragment() {
        return new SetListerFragment();
    }
}
