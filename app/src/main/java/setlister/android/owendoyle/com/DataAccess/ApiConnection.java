package setlister.android.owendoyle.com.DataAccess;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Owen on 01/08/2015.
 */
public abstract class ApiConnection {

    private static final String SETLISTFM_API_HEADER_KEY = "x-api-key";
    private static final String SETLISTFM_CONTENT_HEADER_KEY = "accept";
    private static final String SETLISTFM_API_KEY = "7d12de54-019f-4d2b-bebb-9b42c87b8235";
    private static final String SETLISTFM_CONTENT_TYPE = "application/xml";

  public  byte[] getUrlBytes(String urlSpec) throws IOException {
        HttpsURLConnection connection = getHttpsConnection(urlSpec);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
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

    public  boolean testUrl(String urlSpec) throws IOException{
        HttpsURLConnection connection = getHttpsConnection(urlSpec);
        try {
            InputStream in = connection.getInputStream();
            return true;
        }catch (IOException ioe){
            return false;
        }
        finally {
            connection.disconnect();
        }
    }

     public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    private HttpsURLConnection getHttpsConnection(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        if(urlSpec.contains("api.setlist.fm")){
            connection.setRequestProperty(SETLISTFM_API_HEADER_KEY, SETLISTFM_API_KEY);
            connection.setRequestProperty(SETLISTFM_CONTENT_HEADER_KEY, SETLISTFM_CONTENT_TYPE);
        }
        return connection;
    }
}
