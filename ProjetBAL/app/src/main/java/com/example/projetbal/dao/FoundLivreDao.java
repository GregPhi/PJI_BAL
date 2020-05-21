package com.example.projetbal.dao;

import com.example.projetbal.object.FoundLivre;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FoundLivreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FoundLivre foundLivre);

    @Update
    void updateBook(FoundLivre... foundLivres);

    @Delete
    void delete(FoundLivre foundLivre);

    @Query("DELETE FROM book_table")
    void deleteAll();
}
