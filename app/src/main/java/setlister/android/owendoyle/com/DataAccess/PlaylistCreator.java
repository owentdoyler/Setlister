package setlister.android.owendoyle.com.DataAccess;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import setlister.android.owendoyle.com.music.Playlist;

/**
 * Created by Owen on 02/08/2015.
 */
public class PlaylistCreator {

    private static final String TAG = "PlaylistCreator";

    Context mContext;

    public PlaylistCreator(Context context){
        mContext = context;
    }
    public int createPlaylist(Playlist playlist){
        String[] artistNameVariations = playlist.getArtistsVariations();
        Cursor cursor = findSongs(artistNameVariations);
        int order = 1;
        if (cursor != null){
            Uri insertUri = setupPlaylist(playlist.getPlaylistName());
            if (insertUri != null){
                order = addToPlaylist(cursor, insertUri, order, playlist);
                return (order-1);
            }
            else return 0;
        }
        else return 0;
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

    private Uri setupPlaylist(String playlistName){
        deletePlaylist(playlistName);
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
            return order;
    }

    private boolean playlistContains(long id, Uri insertUri){
        String[] field = {
                MediaStore.Audio.Playlists.Members.AUDIO_ID
        };
        String selection = MediaStore.Audio.Playlists.Members.AUDIO_ID+ "=?";
        String[] where = {""+id};
        Cursor c = mContext.getContentResolver().query(insertUri, field, selection, where, null);
        if (c.moveToFirst()){
            return true;
        }
        else return false;

    }

    private void deletePlaylist(String playlistName){
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

    private String getPlaylist(String playlistName){
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

