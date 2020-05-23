package com.example.projetbal.dao;

import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Livre;

import java.util.List;

import androidx.lifecycle.LiveData;
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
    void updateFoundBook(FoundLivre... foundLivres);

    @Delete
    void delete(FoundLivre foundLivre);

    @Query("DELETE FROM foundbook_table")
    void deleteAll();

    @Query("SELECT * FROM foundbook_table")
    LiveData<List<FoundLivre>> getmAllFoundBooks();

    @Query("SELECT * FROM foundbook_table")
    List<FoundLivre> getmAllFoundBooksList();
}
