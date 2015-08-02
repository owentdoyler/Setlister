package setlister.android.owendoyle.com.setlister;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import setlister.android.owendoyle.com.APIaccess.SetlistFmFetcher;
import setlister.android.owendoyle.com.setlist.Setlists;
import setlister.android.owendoyle.com.setlist.Song;

/**
 * Created by Owen on 31/07/2015.
 */
public class ArtistSetlistFragment extends ListFragment {
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

        new fetchSetlisTask().execute(mArtistMbid, mArtistName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((ArtistSetlistActivity)getActivity()).setActionBarTitle(mArtistName);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class fetchSetlisTask extends AsyncTask<String, Void, ArrayList<Song>>{
        @Override
        protected ArrayList<Song> doInBackground(String... params) {
            Setlists setlists = new SetlistFmFetcher().getSetlists(params[0], params[1]);
            ArrayList<Song> songs = setlists.getMostPlayed();
            return songs;
        }

        @Override
        protected void onPostExecute(ArrayList<Song> songs) {
            setEmptyText("No setlists found for: "+mArtistName);
            SetlistAdapter adapter = new SetlistAdapter(songs);
            setListAdapter(adapter);
        }
    }

    private class SetlistAdapter extends ArrayAdapter<Song>{
        public SetlistAdapter(ArrayList<Song> songs){
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_layout, null);
            }

            Song song = getItem(position);

            TextView songname = (TextView) convertView.findViewById(R.id.artist_list_nameTextView);
            songname.setText(song.getTitle());
            return convertView;
        }
    }
}
