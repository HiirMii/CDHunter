package com.example.android.cdhunter.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.cdhunter.model.artist.SimilarArtist;

import java.util.List;

/**
 * Interface for database access on SimilarArtist related operations.
 */
@Dao
public interface SimilarArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSimilarArtists(List<SimilarArtist> similarArtistList);

    @Query("SELECT * FROM similar_artists WHERE userId = :userId")
    List<SimilarArtist> getAllSimilarArtists(String userId);
}
