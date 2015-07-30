package setlister.android.owendoyle.com.setlister;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Owen on 30/07/2015.
 */
public class ArtistListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> artists = new ArrayList<String>();
        for (int i = 0; i < 20; i++){
            artists.add("artist"+i);
        }

        setListAdapter( new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, artists));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
