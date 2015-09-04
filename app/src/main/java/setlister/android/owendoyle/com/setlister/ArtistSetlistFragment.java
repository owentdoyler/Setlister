package setlister.android.owendoyle.com.setlister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import setlister.android.owendoyle.com.DataAccess.PlaylistCreator;
import setlister.android.owendoyle.com.DataAccess.SetlistFmFetcher;
import setlister.android.owendoyle.com.music.Playlist;
import setlister.android.owendoyle.com.music.Setlists;
import setlister.android.owendoyle.com.music.Song;

/**
 * Created by Owen on 31/07/2015.
 */
public class ArtistSetlistFragment extends ListFragment {
    public static final String EXTRA_ARTIST_NAME = "com.owendoyle.android.setlister.artistSetlist.artist_name";
    public static final String EXTRA_ARTIST_MBID = "com.owendoyle.android.setlister.artistSetlist.artist_mbid";
    private static final String DIALOG_DUPLICATE_PLAYLIST = "duplicate_playlist_alert";
    private static final String DIALOG_PLAYLIST_CREATED = "playlist_created_alert";
    private static final int REQUEST_YES = 0;
    private static final String TAG = "ArtistSetlistFragment";

    private String mArtistName;
    private String mArtistMbid;
    private EditText mPlaylitNameEditText;
    private TextView mCreatPlaylist;
    private Playlist mPlaylist;
    private boolean mYes;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_help:
                Intent i = new Intent(getActivity(), HelpActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_artistsetlist, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_YES){
            mYes = data.getBooleanExtra(DuplicatePlaylistAlertFragment.EXTRA_YES, false);
            if (mYes == true){
                createPlaylist(new PlaylistCreator(getActivity()), getActivity().getSupportFragmentManager(), true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlaylist = new Playlist(mArtistName);
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

        mPlaylitNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.playlist_name && !hasFocus){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });


        mCreatPlaylist = (TextView)v.findViewById(R.id.create_playlist);
        mCreatPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaylitNameEditText.clearFocus();
                if (mPlaylist.getPlaylistName() != null && !mPlaylist.getPlaylistName().equals("")) {
                    populatePlaylist();

                    PlaylistCreator creator = new PlaylistCreator(getActivity());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    if (creator.getPlaylist(mPlaylist.getPlaylistName()) == null){
                        createPlaylist(creator, fragmentManager, false);
                    }
                    else {
                        DuplicatePlaylistAlertFragment alertFragment = DuplicatePlaylistAlertFragment.newInstance(mPlaylist.getPlaylistName());
                        alertFragment.setTargetFragment(ArtistSetlistFragment.this, REQUEST_YES);
                        alertFragment.show(fragmentManager, DIALOG_DUPLICATE_PLAYLIST);
                    }

                } else {
                    Toast.makeText(getActivity(), "Enter a name for the playlist", Toast.LENGTH_LONG).show();
                }
            }

        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void populatePlaylist(){
        ArrayList<Song> songs = ((SetlistAdapter) getListAdapter()).getItems();
        for (Song song : songs){
            if (song.isSelected()){
                mPlaylist.addSong(song);
            }
        }
    }

    private void createPlaylist(PlaylistCreator creator, FragmentManager fragmentManager, boolean overwrite){

            int result  = creator.createPlaylist(mPlaylist, overwrite);
            PlaylistCreatedAlertFragment playlistCreatedAlert = PlaylistCreatedAlertFragment
                    .newInstace(result, mPlaylist.getSongs().size(), mPlaylist.getPlaylistName(), mArtistName);
            playlistCreatedAlert.show(fragmentManager, DIALOG_PLAYLIST_CREATED);
            mPlaylist.clearPlaylist();
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

            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            final Song song = getItem(position);

            TextView songname = (TextView) convertView.findViewById(R.id.song_list_nameTextView);
            songname.setText(song.getTitle());

            final CheckBox songSelected = (CheckBox) convertView.findViewById(R.id.song_selected_checkbox);
            songSelected.setChecked(song.isSelected());
            songSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = songSelected.isChecked();
                    song.setSelected(isChecked);
                    insert(song, position);
                }
            });
            return convertView;
        }
    }
}
