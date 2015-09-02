package setlister.android.owendoyle.com.setlister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Owen on 01/09/2015.
 */
public class Splash extends Activity {
    private static int SPLASH_TIME = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, SetListerActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME);
    }
}
