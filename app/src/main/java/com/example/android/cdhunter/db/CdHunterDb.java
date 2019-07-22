package com.example.android.cdhunter.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.model.artist.SimilarArtist;
import com.example.android.cdhunter.model.artist.Tag;

import timber.log.Timber;

/**
 * Main database description.
 */
@Database(entities = {Album.class, Tag.class, SimilarArtist.class}, version = 1, exportSchema = false)
public abstract class CdHunterDb extends RoomDatabase {

    private static final String LOG_TAG = CdHunterDb.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "cdhunter";
    private static CdHunterDb cdHunterDbInstance;

    public static CdHunterDb getInstance(Context context) {
        if (cdHunterDbInstance == null) {
            synchronized (LOCK) {
                Timber.tag(LOG_TAG).d("Creating new database instance");
                cdHunterDbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        CdHunterDb.class, CdHunterDb.DATABASE_NAME)
                        // Queries should be done in a separate thread to avoid locking the UI
                        // I will allow this ONLY TEMPORALLY to see if DB is working
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Timber.tag(LOG_TAG).d("Getting the database instance");
        return cdHunterDbInstance;
    }

    public abstract AlbumDao albumDao();

    public abstract TagDao tagDao();

    public abstract SimilarArtistDao similarArtistDao();
}
