package com.example.projetbal.dao;

import com.example.projetbal.object.book.Livre;
import com.example.projetbal.object.book.Matiere;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MatiereDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Matiere matiere);

    @Update
    void updateBook(Matiere... matiere);

    @Delete
    void delete(Matiere matiere);

    @Query("DELETE FROM matiere_table")
    void deleteAll();

    @Query("SELECT * FROM matiere_table ORDER BY nom ASC")
    LiveData<List<Matiere>> getmAllMatieres();

    @Query("SELECT * FROM matiere_table ORDER BY nom ASC")
    List<Matiere> getmAllMatieresForSpinner();
}
