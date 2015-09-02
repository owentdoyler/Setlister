package setlister.android.owendoyle.com.DataAccess;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import setlister.android.owendoyle.com.music.Playlist;
import setlister.android.owendoyle.com.music.Song;

/**
 * Created by Owen on 02/08/2015.
 */
public class PlaylistCreator {

    public static final int NO_ARTIST = -1;
    public static final int NO_SONGS = -2;
    public static final int ERROR = -3;

    private static final String TAG = "PlaylistCreator";

    Context mContext;

    public PlaylistCreator(Context context){
        mContext = context;
    }
    public int createPlaylist(Playlist playlist, boolean overwrite){
        String[] artistNameVariations = playlist.getArtistsVariations();
        Cursor cursor = findSongs(artistNameVariations);
        int order = 1;
        if (cursor != null){ //if any artists matching the playlist artist was found

            if (anySongsFound(cursor,playlist)){ //if any of the songs selected were found

                Uri insertUri = setupPlaylist(playlist.getPlaylistName(), overwrite);
                if (insertUri != null){ //if the playlist was setup correctly
                    order = addToPlaylist(cursor, insertUri, order, playlist);
                    cursor.close();
                    return (order - 1); // return the number of songs that were found and added to the playlist

                }
                else{
                    cursor.close();
                    return ERROR;
                }
            }
            else {
                cursor.close();
                return NO_SONGS;
            }
        }
        else {
            return NO_ARTIST;
        }
    }

    private Cursor findSongs(String[] artistNameVariations){

        for (String st : artistNameVariations){
            Log.d(TAG, "ArtistName: "+st);
        }
        String[] columns = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media._ID
        };
        String[] args = {
                artistNameVariations[0],
                artistNameVariations[1],
                artistNameVariations[2],
                artistNameVariations[3]
        };

        Uri allSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND ("
                +MediaStore.Audio.Media.ARTIST+"=? OR "
                +MediaStore.Audio.Media.ARTIST+"=? OR "
                +MediaStore.Audio.Media.ARTIST+"=? OR "
                +MediaStore.Audio.Media.ARTIST+"=? )";

        Cursor cursor = mContext.getContentResolver().query(allSongUri, columns, selection, args, null);

        if (cursor.moveToFirst()){
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            Log.d("got title", title);
            return cursor;
        }
        else return null;
    }

    private Uri setupPlaylist(String playlistName, boolean overwrite){
        if (overwrite){
            deletePlaylist(playlistName);
        }
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues insert = new ContentValues();
        insert.put(MediaStore.Audio.Playlists.NAME, playlistName);
        insert.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
        insert.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
        Uri newPlaylistUri = resolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, insert);
        if (newPlaylistUri != null){
            return Uri.withAppendedPath(newPlaylistUri, MediaStore.Audio.Playlists.Members.CONTENT_DIRECTORY);
        }
        else {
            Log.e(TAG, "Failed to create playlist: "+playlistName);
            return null;
        }
    }

    private int addToPlaylist(Cursor cursor, Uri insertUri, int order, Playlist playlist){
        ContentResolver resolver = mContext.getContentResolver();
            do {
                String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                if (playlist.containsSong(songName)){
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    if (!playlistContains(id, insertUri)){
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, order++);
                        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, id);
                        resolver.insert(insertUri, values);
                    }
                }
            }while (cursor.moveToNext());
        cursor.close();
            return order;
    }

    private boolean anySongsFound(Cursor cursor, Playlist playlist){
        cursor.moveToFirst();
        do {
            String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            if (playlist.containsSong(songName)){
                return true;
            }
        }while (cursor.moveToNext());
        return false;
    }

    private boolean playlistContains(long id, Uri insertUri){
        String[] field = {
                MediaStore.Audio.Playlists.Members.AUDIO_ID
        };
        String selection = MediaStore.Audio.Playlists.Members.AUDIO_ID+ "=?";
        String[] where = {""+id};
        Cursor c = mContext.getContentResolver().query(insertUri, field, selection, where, null);
        if (c.moveToFirst()){
            c.close();
            return true;
        }
        else {
            c.close();
            return false;
        }
    }

    public void deletePlaylist(String playlistName){
        String playlistId = getPlaylist(playlistName);
        if (playlistId != null){
            ContentResolver resolver = mContext.getContentResolver();
            String query = MediaStore.Audio.Playlists._ID + "=?";
            String[] where = {
                    playlistId
            };
            resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, query, where);
        }
    }

    public String getPlaylist(String playlistName){
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        String id = MediaStore.Audio.Playlists._ID;
        String name = MediaStore.Audio.Playlists.NAME;

        String query = MediaStore.Audio.Playlists.NAME + "=?";
        String[] projection = {
                id,
                name
        };
        String[] where = {
                playlistName
        };

        Cursor cursor = mContext.getContentResolver().query(uri, projection, query, where, null);

        String playlistId = null;

        if (cursor.moveToFirst()){
            playlistId = cursor.getString(cursor.getColumnIndex(id));
            cursor.close();
        }
        else {
            cursor.close();
        }
        return playlistId;
    }
}

