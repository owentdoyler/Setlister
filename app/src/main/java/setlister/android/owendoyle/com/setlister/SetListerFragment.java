package setlister.android.owendoyle.com.setlister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Owen on 28/07/2015.
 */
public class SetListerFragment extends Fragment {

    private static final String TAG ="SetListerFragment";

    private EditText mArtistSearch;
    private Button mGoButton;
    private String mArtistName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_set_lister, container, false);

        mArtistSearch = (EditText) v.findViewById(R.id.artist_search_box);
        mArtistSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mArtistName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mGoButton = (Button)v.findViewById(R.id.go_button);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ArtistListActivity.class);
                startActivity(i);
                Log.d(TAG, "artist name: " + mArtistName);
            }
        });

        return v;
    }
}
