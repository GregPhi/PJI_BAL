package com.example.projetbal.dataB;

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

    public LiveData<List<Livre>> getAllBookForAMatiere(String m){ return mRepository.getAllBookForAMatiere(m); }

    public LiveData<List<Livre>> findBookWithCodeBarre(String i){ return mRepository.findBookWithCodeBarre(i); }

    public void insert(Livre book) { mRepository.insert(book); }

    public void updateBook(Livre... books){
        mRepository.updateBook(books[0]);
    }

    public void delete(Livre book) { mRepository.delete(book); }

    public void deleteAll(){ mRepository.deleteAll(); }

}

