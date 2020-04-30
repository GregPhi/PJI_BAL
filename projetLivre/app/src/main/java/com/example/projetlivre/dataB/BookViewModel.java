package com.example.projetlivre.dataB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projetlivre.object.Book;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository mRepository;

    private LiveData<List<Book>> mAllBooks;

    public BookViewModel(Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllBooks = mRepository.getmAllBooks();
    }

    public LiveData<List<Book>> getmAllBooks() { return mAllBooks; }

    public LiveData<List<Book>> getAllBookForAMatiere(String m){ return mRepository.getAllBookForAMatiere(m); }

    public LiveData<List<Book>> findBookWithCodeBarre(String i){ return mRepository.findBookWithCodeBarre(i); }

    public void insert(Book book) { mRepository.insert(book); }

    public void updateBook(Book... books){
        mRepository.updateBook(books[0]);
    }

    public void delete(Book book) { mRepository.delete(book); }

    public void deleteAll(){ mRepository.deleteAll(); }

}

