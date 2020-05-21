package com.example.projetbal.dataB.book;

import android.app.Application;

import com.example.projetbal.object.Livre;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BookViewModel extends AndroidViewModel {
    private BookRepository mRepository;

    private LiveData<List<Livre>> mAllBooks;

    public BookViewModel(Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllBooks = mRepository.getmAllBooks();
    }

    public LiveData<List<Livre>> getmAllBooks() { return mAllBooks; }

    public List<Livre> getmAllBooksForJson(){ return mRepository.getmAllBooksForJson(); }

    public LiveData<List<Livre>> getAllBookForAMatiere(String m){ return mRepository.getAllBookForAMatiere(m); }

    public LiveData<List<Livre>> findBookWithCodeBarre(String i){ return mRepository.findBookWithCodeBarre(i); }

    public List<Livre> findBookWithCodeBarreList(String c){ return mRepository.findBookWithCodeBarreList(c); }

    public void insert(Livre book) { mRepository.insert(book); }

    public void updateBook(Livre... books){
        mRepository.updateBook(books[0]);
    }

    public void delete(Livre book) { mRepository.delete(book); }

    public void deleteAll(){ mRepository.deleteAll(); }

}

