package setlister.android.owendoyle.com.setlister;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Owen on 10/08/2015.
 */
public class ConnectionInformation {

    private static final String TAG = "ConnectionInformation";

    private static NetworkInfo mNetworkInfo;

    public static boolean hasInternetAccess(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            mNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        if(mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()){
            return true;
        }

        mNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()){
            return true;
        }

        return false;
    }
}
