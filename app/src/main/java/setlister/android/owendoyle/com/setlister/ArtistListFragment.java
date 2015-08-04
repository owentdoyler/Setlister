package setlister.android.owendoyle.com.setlister;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import setlister.android.owendoyle.com.DataAccess.MusicBrainzFetcher;
import setlister.android.owendoyle.com.music.Artist;

/**
 * Created by Owen on 30/07/2015.
 */
public class ArtistListFragment extends ListFragment {

    private static final boolean DEBUG = false;
    private static final String TAG = "ArtistListFragment";
    public static final String EXTRA_ARTIST_NAME = "com.owendoyle.android.setlister.artist_name";

    private String mArtistSearchName;
    private ArrayList<Artist> mArtists;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String artistName;
        if(savedInstanceState == null){
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null){
                artistName = null;
            }
            else {
                artistName = extras.getString(EXTRA_ARTIST_NAME);
            }
        }
        else {
            artistName = (String) savedInstanceState.getSerializable(EXTRA_ARTIST_NAME);
        }
        mArtistSearchName = artistName;

        new fetchArtistsTask().execute(mArtistSearchName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((ArtistListActivity)getActivity()).setActionBarTitle("Showing results for: " + mArtistSearchName);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Artist artist = ((ArtistAdapter)getListAdapter()).getItem(position);
        String artistName = artist.getName();
        String artistMbid = artist.getMbid();
        if (DEBUG){
            Log.d(TAG, "Artist selected(artist name: "+artistName + " artist mbid: "+ artistMbid +")");
        }
        Intent i = new Intent(getActivity(), ArtistSetlistActivity.class);
        i.putExtra(ArtistSetlistFragment.EXTRA_ARTIST_NAME, artistName);
        i.putExtra(ArtistSetlistFragment.EXTRA_ARTIST_MBID, artistMbid);
        startActivity(i);
    }

    private class fetchArtistsTask extends AsyncTask<String, Void, ArrayList<Artist>> {

        @Override
        protected ArrayList<Artist> doInBackground(String... params) {
            ArrayList<Artist> artists = new MusicBrainzFetcher().searchForArtists(params[0]);
            if (DEBUG){
                for(Artist artist : artists){
                    Log.i(TAG, "name: " + artist.getName() + "  mbid: " + artist.getMbid());
                }
            }
            return artists;
        }

        @Override
        protected void onPostExecute(ArrayList<Artist> artists) {
            setEmptyText("No artists found for the name: \""+mArtistSearchName+"\"");
            mArtists = artists;
            ArtistAdapter adapter = new ArtistAdapter(mArtists);
            setListAdapter(adapter);

        }
    }

    private class ArtistAdapter extends ArrayAdapter<Artist>{

        public ArtistAdapter(ArrayList<Artist> artists){
            super(getActivity(), 0, artists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_layout, null);
            }
            Artist artist = getItem(position);

            TextView artistNameTextView = (TextView) convertView.findViewById(R.id.artist_list_nameTextView);
            if (artist.getDisambiguation().equals("")){
                artistNameTextView.setText(artist.getName());
            }
            else {
                artistNameTextView.setText(artist.getName()+"("+artist.getDisambiguation()+")");
            }

            return convertView;
        }
    }

}
