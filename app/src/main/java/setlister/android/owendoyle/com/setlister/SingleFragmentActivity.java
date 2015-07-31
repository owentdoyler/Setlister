package setlister.android.owendoyle.com.setlister;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Owen on 02/07/2015.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    TextView mActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    public void setActionBarTitle(String title){
        mActionBarTitle.setText(title);
    }
}
