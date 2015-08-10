package setlister.android.owendoyle.com.setlister;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Owen on 05/08/2015.
 */
public class DuplicatePlaylistAlertFragment extends DialogFragment {
    public static final String EXTRA_NAME = "setlister.android.owendoyle.com.setlister.playlistName";
    public  static final String EXTRA_YES = "setlister.android.owendoyle.com.setlister.yes";

    private String mPlaylistName;
    private boolean mYes;

    public static DuplicatePlaylistAlertFragment newInstance(String playlistName){
        Bundle args = new Bundle();
        args.putString(EXTRA_NAME, playlistName);
        DuplicatePlaylistAlertFragment fragment = new DuplicatePlaylistAlertFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_YES, mYes);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mPlaylistName = getArguments().getString(EXTRA_NAME);
        View view = getActivity().getLayoutInflater().inflate(R.layout.duplicate_playlist_alert_layout, null);

        TextView alertText = (TextView)view.findViewById(R.id.duplicate_playlist_alert_text);
        alertText.setText("A playlist already exists with the name: '" + mPlaylistName + "' do you want to overwrite it to make this new one?");

        Button yesButton = (Button)view.findViewById(R.id.duplicate_playlist_alert_yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYes = true;
                sendResult(Activity.RESULT_OK);
                dismiss();
            }
        });
        Button noButton = (Button)view.findViewById(R.id.duplicate_playlist_alert_no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYes = false;
                sendResult(Activity.RESULT_OK);
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(view).create();

    }
}
