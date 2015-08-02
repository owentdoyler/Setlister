package setlister.android.owendoyle.com.APIaccess;

import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import setlister.android.owendoyle.com.music.*;
import setlister.android.owendoyle.com.music.Setlists;

/**
 * Created by Owen on 31/07/2015.
 */
public class SetlistFmFetcher extends ApiConnection{

    private static final boolean DEBUG = false;
    private static final String TAG = "SetlistFmFetcher";
    private static final String ROOT = "http://api.setlist.fm/rest/0.1/artist/";
    private static final String URL_END = "setlists";
    private static final String XML_OPENING = "setlists";

    private String mArtistName;
    private String mMbid;

    public boolean checkArtist(String mbid){
        mMbid = mbid;
        String url = Uri.parse(ROOT).buildUpon().appendPath(mMbid).appendPath(URL_END).build().toString();
            boolean check = testUrl(url);
            Log.d(TAG,""+check);
            return check;

    }

    public Setlists getSetlists(String mbid, String artistName){
        mArtistName = artistName;
        mMbid = mbid;
        Setlists setlists = new Setlists();

        String url = Uri.parse(ROOT).buildUpon().appendPath(mMbid).appendPath(URL_END).build().toString();
        if(DEBUG){
            Log.d(TAG, "Got url: " + url);
        }

        try {
            String xml = new String(getUrlBytes(url));
            setlists = parseXml(xml);
        }catch (IOException ioe){
            Log.d(TAG, "failed to get setlistFm xml", ioe);
        }

        if (DEBUG){
            Log.d(TAG, setlists.toString());
        }
        return setlists;
    }

    private Setlists parseXml(String xml){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            return parseItems(parser);
        }catch (XmlPullParserException xppe) {
            Log.d(TAG, "Failed to instantiate Parser", xppe);
            return new Setlists();
        }catch (IOException ioe){
            Log.d(TAG, "Failed to instantiate parser");
            return new Setlists();
        }
    }

    private Setlists parseItems(XmlPullParser parser)throws XmlPullParserException, IOException{
        Setlists setlists = new Setlists();
        if (parser != null){
            while (parser.next() != XmlPullParser.END_TAG){
                if (parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }
                String name = parser.getName();
                if (name.equals(XML_OPENING)){
                    continue;
                }
                else if (name.equals("setlist")){
                    setlists.addSetlist(parseSetlist(parser));
                }
                else {
                    skip(parser);
                }
            }
        }
        else {
            return setlists;
        }

        return setlists;
    }

    private Setlist parseSetlist(XmlPullParser parser) throws XmlPullParserException, IOException{
        Setlist setlist = new Setlist();
        parser.require(XmlPullParser.START_TAG, null, "setlist");
        String artistName;
        String songTitle;
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals("sets")){
                setlist = parseSets(parser);
            }
            else {
                skip(parser);
            }
        }
        return setlist;
    }

    private Setlist parseSets(XmlPullParser parser) throws XmlPullParserException, IOException{
        Setlist setlist = new Setlist();
        parser.require(XmlPullParser.START_TAG, null, "sets");

        while (parser.next() !=  XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals("set")){
                setlist.addSet(parseSet(parser));
            }
            else {
                skip(parser);
            }
        }
        return setlist;
    }

    private Set parseSet(XmlPullParser parser) throws XmlPullParserException, IOException{
        Set set = new Set();
        parser.require(XmlPullParser.START_TAG, null, "set");
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("song")){
                set.addSong(parseSong(parser));
            }
            else {
                skip(parser);
            }
        }
        return set;
    }

    private Song parseSong(XmlPullParser parser)throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, null, "song");
        String songTitle = parser.getAttributeValue(null, "name");
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            skip(parser);
        }
        return new Song(songTitle, mArtistName);
    }

}
