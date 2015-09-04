package setlister.android.owendoyle.com.setlister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Owen on 28/07/2015.
 */
public class SetListerFragment extends Fragment {

    private static final String TAG ="SetListerFragment";

    private EditText mArtistSearch;
    private Button mGoButton;
    private String mArtistName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_set_lister, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                Intent i = new Intent(getActivity(), AboutActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_lister, container, false);
        ((SetListerActivity)getActivity()).setActionBarTitle("Setlister");

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

        mArtistSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.artist_search_box && !hasFocus){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        mGoButton = (Button)v.findViewById(R.id.go_button);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArtistSearch.clearFocus();
                if (((SetListerActivity) getActivity()).hasInternrtAccess()){
                    if (mArtistName != null && !mArtistName.equals("")){
                        Intent i = new Intent(getActivity(), ArtistListActivity.class);
                        i.putExtra(ArtistListFragment.EXTRA_ARTIST_NAME, mArtistName);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(getActivity(), R.string.artist_search_empty_toast, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), R.string.no_connection_toast, Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

}
