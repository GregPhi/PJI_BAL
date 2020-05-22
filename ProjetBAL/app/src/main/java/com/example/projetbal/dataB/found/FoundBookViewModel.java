package com.example.projetbal.dataB.found;

import android.app.Application;

import com.example.projetbal.object.FoundLivre;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FoundBookViewModel extends AndroidViewModel {
    private FoundBookRepository mRepository;

    private LiveData<List<FoundLivre>> mAllBooks;

    public FoundBookViewModel(Application application) {
        super(application);
        mRepository = new FoundBookRepository(application);
        mAllBooks = mRepository.getmAllFoundBooks();
    }

    public LiveData<List<FoundLivre>> getmAllFoundBooks() { return mAllBooks; }

    public List<FoundLivre> getmAllFoundBooksList(){ return mRepository.getmAllFoundBooksList(); }

    public void insert(FoundLivre book) { mRepository.insert(book); }

    public void updateBook(FoundLivre... books){
        mRepository.updateBook(books[0]);
    }

    public void delete(FoundLivre book) { mRepository.delete(book); }

    public void deleteAll(){ mRepository.deleteAll(); }

}
