package com.example.android.cdhunter.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.model.artist.SimilarArtist;
import com.example.android.cdhunter.model.artist.Tag;

/**
 * Main database description.
 */
@Database(entities = {Album.class, Tag.class, SimilarArtist.class}, version = 1, exportSchema = false)
public abstract class CdHunterDb extends RoomDatabase {


// --- DAO METHODS ---

    public abstract AlbumDao albumDao();

    public abstract TagDao tagDao();

    public abstract SimilarArtistDao similarArtistDao();
}
