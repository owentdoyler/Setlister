package setlister.android.owendoyle.com.setlister;


import android.net.Uri;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by Owen on 30/07/2015.
 */
public class ArtistLibrary {

    private static final String TAG = "ArtistLibrary";
    private static final String ROOT = "http://musicbrainz.org/ws/2/artist/";
    private static final String QUERY = "?query=artist:";
    private static final String XML_FIRST_TAG = "metadata";
    private static final String XML_SECOND_TAG = "artist-list";
    private static final String TAG_NEEDED = "artist";
    private static final String ARTIST_NAME = "name";
    private static final String ARTIST_MBID = "id";

    public ArrayList<Artist> searchForArtists(String artistName){
        ArrayList<Artist> Artists = new ArrayList<Artist>();
        artistName = artistName.replace(" ", "+");

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


    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    private ArrayList<Artist> parseXml(String xml){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            return parseItems(parser);
        }catch (XmlPullParserException xppe){
            Log.d(TAG, "Failed to instansiate parser",xppe);
            return new ArrayList<Artist>();
        }catch (IOException ioe){
            Log.d(TAG, "Failed to instansiate parser", ioe);
            return new ArrayList<Artist>();
        }
    }

    private ArrayList<Artist> parseItems(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<Artist> artists = new ArrayList<Artist>();
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
                    artists.add(parseTag(parser));
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

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals(ARTIST_NAME)){
                artistName = readArtistName(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Artist(artistName, mbid);
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

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
