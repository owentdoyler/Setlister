package setlister.android.owendoyle.com.setlister;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import setlister.android.owendoyle.com.DataAccess.PlaylistCreator;

/**
 * Created by Owen on 05/08/2015.
 */
public class PlaylistCreatedAlertFragment extends DialogFragment {

    public static final String EXTRA_RESULT = "setlister.androis.owendoyle.setlister.result";
    public static final String EXTRA_PLAYLIST_NAME = "setlister.androis.owendoyle.setlister.playlistName";
    public static final String EXTRA_PLAYLIST_SIZE = "setlister.androis.owendoyle.setlister.playlistSize";
    public static final String EXTRA_ARTIST_NAME = "setlister.androis.owendoyle.setlister.artistName";
    private static final String TAG = "PlaylistCreatedAlertFragment";

    private String mplaylistName;
    private String mArtistName;
    private int mResult;
    private int mPlaylistSize;

    public static PlaylistCreatedAlertFragment newInstace(int result, int playlistSize, String playlistName, String artistName){
        Bundle args = new Bundle();
        args.putInt(EXTRA_RESULT, result);
        args.putInt(EXTRA_PLAYLIST_SIZE, playlistSize);
        args.putString(EXTRA_PLAYLIST_NAME, playlistName);
        args.putString(EXTRA_ARTIST_NAME,artistName);
        PlaylistCreatedAlertFragment fragment = new PlaylistCreatedAlertFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mResult = getArguments().getInt(EXTRA_RESULT);
        mPlaylistSize = getArguments().getInt(EXTRA_PLAYLIST_SIZE);
        mplaylistName = getArguments().getString(EXTRA_PLAYLIST_NAME);
        mArtistName = getArguments().getString(EXTRA_ARTIST_NAME);
        String[] alertStrings = getAlertStrings();

        View view = getActivity().getLayoutInflater().inflate(R.layout.playlist_created_alert, null);

        TextView alertTitle = (TextView)view.findViewById(R.id.create_playlist_alertTitle);
        alertTitle.setText(alertStrings[0]);

        TextView alertText = (TextView)view.findViewById(R.id.create_playlist_alertText);
        alertText.setText(alertStrings[1]);

        Button okButton = (Button)view.findViewById(R.id.alert_ok_button_id);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetListerActivity.class);
                dismiss();
                startActivity(intent);
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }

    private String[] getAlertStrings(){
        String[] resultStrings = new String[2];

        if (mResult == PlaylistCreator.ERROR){
            resultStrings[0] = getString(R.string.playlist_create_error_title);
            resultStrings[1] = getString(R.string.playlist_create_error_text);
        }
        else if (mResult == PlaylistCreator.NO_SONGS){
            resultStrings[0] = getString(R.string.playlist_not_created_title);
            resultStrings[1] = getString(R.string.playlist_not_created_song_text);
        }
        else if (mResult == PlaylistCreator.NO_ARTIST){
            resultStrings[0] = getString(R.string.playlist_not_created_title);
            resultStrings[1] = getString(R.string.playlist_not_created_artist_text1)+" '"
                    + mArtistName+"' "
                    + getString(R.string.playlist_not_created_artist_text2);
        }
        else if (mResult > 0){
            String result = "Found "+mResult+" out of "+mPlaylistSize+" songs.\n";
            resultStrings[0] = getString(R.string.playlist_created_title);
            resultStrings[1] = result + getString(R.string.playlist_created_text1)+" '"
                    +mplaylistName+"' "
                    +getString(R.string.playlist_created_text2);
        }
        else {
            return null;
        }
        return resultStrings;
    }
}
