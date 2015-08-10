package setlister.android.owendoyle.com.DataAccess;


import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import setlister.android.owendoyle.com.music.Artist;

/**
 * Created by Owen on 30/07/2015.
 */
public class MusicBrainzFetcher extends ApiConnection{

    private static final String TAG = "MusicBrainzFetcher";
    private static final String ROOT = "http://musicbrainz.org/ws/2/artist/";
    private static final String QUERY = "?query=artist:";
    private static final String XML_FIRST_TAG = "metadata";
    private static final String XML_SECOND_TAG = "artist-list";
    private static final String TAG_NEEDED = "artist";
    private static final String ARTIST_NAME = "name";
    private static final String ARTIST_MBID = "id";

    public ArrayList<Artist> searchForArtists(String artistName){
        ArrayList<Artist> Artists = new ArrayList<Artist>();
        if (artistName != null){
            artistName = artistName.replace(" ", "+");
        }

        try {
            artistName = URLEncoder.encode(artistName, "UTF-8");
        }catch (UnsupportedEncodingException uee){
            Log.d(TAG, "Failed to encode artist name", uee);
        }

        // create the url to search for artist from the musicbrainz api
        String url = Uri.parse(ROOT).buildUpon().appendEncodedPath(QUERY + artistName).build().toString();
        try {
            //get the xml from the url
            String xml = new String(getUrlBytes(url));
            //get the artists from the xml
            Artists = parseXml(xml);
        }catch (IOException ioe){
            Log.d(TAG, "failed to get xml..", ioe);
        }


        return Artists;
    }

    private ArrayList<Artist> parseXml(String xml){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            return parseItems(parser);
        }catch (XmlPullParserException xppe){
            Log.d(TAG, "Failed to instantiate parser",xppe);
            return new ArrayList<Artist>();
        }catch (IOException ioe){
            Log.d(TAG, "Failed to instantiate parser", ioe);
            return new ArrayList<Artist>();
        }
    }

    private ArrayList<Artist> parseItems(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<Artist> artists = new ArrayList<Artist>();
        SetlistFmFetcher setlistFmFetcher = new SetlistFmFetcher();
        if (parser != null){
            while (parser.next() != XmlPullParser.END_TAG){
                if (parser.getEventType() != XmlPullParser.START_TAG){
                    //check that the current tag being looked at in the xml is a start tag
                    continue;
                }
                String name = parser.getName();
                if(name.equals(XML_FIRST_TAG) || name.equals(XML_SECOND_TAG)){
                    //skip the first 2 tags in the xml document
                    continue;
                }
                else if (name.equals(TAG_NEEDED)){
                    Artist artist = parseTag(parser);
                    if (setlistFmFetcher.checkArtist(artist.getMbid())){
                        artists.add(artist);
                    }
                }
                else {
                    skip(parser);
                }
            }

            return artists;
        }
        else {
            return artists;
        }
    }

    private Artist parseTag(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, null, TAG_NEEDED);
        String artistName = null;

        String mbid = parser.getAttributeValue(null, ARTIST_MBID);
        String disambiguation = "";

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals(ARTIST_NAME)){
                artistName = readArtistName(parser);
            }
            else if (name.equals("disambiguation")){
                disambiguation = readDisambiguation(parser);
            }
            else {
                skip(parser);
            }
        }
        Artist artist = new Artist(artistName, mbid);
        artist.setDisambiguation(disambiguation);
        return artist;
    }

    private String readDisambiguation(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, null, "disambiguation");
        String dis = "";
        if (parser.next() == XmlPullParser.TEXT){
            dis = parser.getText();
            parser.nextTag();
        }
        return dis;
    }

    private String readArtistName(XmlPullParser parser) throws XmlPullParserException,IOException {
        parser.require(XmlPullParser.START_TAG, null, ARTIST_NAME);
        String name = "";
        if(parser.next() == XmlPullParser.TEXT){
            name = parser.getText();
            parser.nextTag();
        }
        return name;
    }

}
