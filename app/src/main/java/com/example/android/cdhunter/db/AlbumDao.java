package com.example.android.cdhunter.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.cdhunter.model.album.Album;

import java.util.List;

/**
 * Interface for database access on Album related operations.
 */
@Dao
public interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAlbums(List<Album> albumList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleAlbum(Album album);

    @Query("SELECT * FROM albums WHERE userId = :userId AND ownershipStatus = :ownershipStatus " +
            "ORDER BY artistName, albumName")
    LiveData<List<Album>> getAllAlbums(String userId, String ownershipStatus);

    @Query("SELECT * FROM albums WHERE userId = :userId AND albumId = :albumId")
    LiveData<Album> getSingleAlbum(String userId, String albumId);

    @Query("SELECT * FROM albums WHERE userId = :userId AND albumId = :albumId LIMIT 1")
    Album checkIfAlbumExists(String userId, String albumId);

    @Query("UPDATE albums SET ownershipStatus = :ownershipStatus WHERE userId = :userId AND albumId = :albumId")
    void updateSingleAlbumOwnershipStatus(String userId, String albumId, String ownershipStatus);
}