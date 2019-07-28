package com.example.android.cdhunter.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.cdhunter.model.common.Tag;

import java.util.List;

/**
 * Interface for database access on Tag related operations.
 */
@Dao
public interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTags(List<Tag> tagList);

    @Query("SELECT * FROM tags WHERE userId = :userId")
    List<Tag> getAllTags(String userId);
}
