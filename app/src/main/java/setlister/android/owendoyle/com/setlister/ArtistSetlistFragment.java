package setlister.android.owendoyle.com.setlister;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import setlister.android.owendoyle.com.APIaccess.SetlistFmFetcher;
import setlister.android.owendoyle.com.music.Playlist;
import setlister.android.owendoyle.com.music.Setlists;
import setlister.android.owendoyle.com.music.Song;

/**
 * Created by Owen on 31/07/2015.
 */
public class ArtistSetlistFragment extends ListFragment {
    public static final String EXTRA_ARTIST_NAME = "com.owendoyle.android.setlister.artistSetlist.artist_name";
    public static final String EXTRA_ARTIST_MBID = "com.owendoyle.android.setlister.artistSetlist.artist_mbid";

    private static final String TAG = "ArtistSetlistFragment";

    private String mArtistName;
    private String mArtistMbid;
    private EditText mPlaylitNameEditText;
    private TextView mCreatPlaylist;
    private Playlist mPlaylist;
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
        mPlaylist = new Playlist();
        View v = ((ArtistSetlistActivity)getActivity()).getSupportActionBar().getCustomView();
        mPlaylitNameEditText = (EditText)v.findViewById(R.id.playlist_name);
        mPlaylitNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlaylist.setPlaylistName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCreatPlaylist = (TextView)v.findViewById(R.id.create_playlist);
        mCreatPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaylist.getPlaylistName() != null && !mPlaylist.getPlaylistName().equals("")){
                    Toast.makeText(getActivity(), "making playlist: "+mPlaylist.getPlaylistName(), Toast.LENGTH_LONG).show();
                    ArrayList<Song> songs = ((SetlistAdapter) getListAdapter()).getItems();
                    for (Song song : songs){
                        if (song.isSelected()){
                            mPlaylist.addSong(song);
                        }
                    }

                }
                else {
                    Toast.makeText(getActivity(), "Enter a name for the playlist", Toast.LENGTH_LONG).show();
                }
            }

        });

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
        private ArrayList<Song> mSongs;
        public SetlistAdapter(ArrayList<Song> songs){
            super(getActivity(), 0, songs);
            this.mSongs = songs;
        }

        public ArrayList<Song> getItems(){
            return this.mSongs;
        }

        @Override
        public Song getItem(int position) {
            return this.mSongs.get(position);
        }

        @Override
        public void insert(Song object, int index) {
            this.mSongs.set(index, object);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.playlist_item_layout, null);
            }

            final Song song = getItem(position);

            TextView songname = (TextView) convertView.findViewById(R.id.song_list_nameTextView);
            songname.setText(song.getTitle());

            CheckBox songSelected = (CheckBox) convertView.findViewById(R.id.song_selected_checkbox);
            songSelected.setChecked(song.isSelected());
            songSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    song.setSelected(isChecked);
                    insert(song, position);
                }
            });
            return convertView;
        }
    }
}
