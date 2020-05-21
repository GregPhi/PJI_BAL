package com.example.projetbal.dao;

import com.example.projetbal.object.Livre;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LivreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Livre book);

    @Update
    void updateBook(Livre... books);

    @Delete
    void delete(Livre book);

    @Query("DELETE FROM book_table")
    void deleteAll();

    @Query("SELECT * FROM book_table ORDER BY title ASC")
    LiveData<List<Livre>> getmAllBooks();

    @Query("SELECT * FROM book_table ORDER BY title ASC")
    List<Livre> getmAllBooksForJson();

    @Query("SELECT * FROM book_table WHERE matiere =:m")
    LiveData<List<Livre>> getAllBookForAMatiere(String m);

    @Query("SELECT * FROM book_table WHERE id =:i")
    LiveData<List<Livre>> findBookWithCodeBarre(String i);

    @Query("SELECT * FROM book_table WHERE code_barre =:c")
    List<Livre> findBookWithCodeBarreList(String c);
}
