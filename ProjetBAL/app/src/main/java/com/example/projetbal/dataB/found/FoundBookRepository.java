package com.example.projetbal.dataB.found;

import android.app.Application;
import android.os.AsyncTask;

import com.example.projetbal.ProjectRoomDatabase;
import com.example.projetbal.dao.FoundLivreDao;
import com.example.projetbal.dataB.book.BookRepository;
import com.example.projetbal.object.FoundLivre;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FoundBookRepository {
    private FoundLivreDao mFoundLivreDao;
    private LiveData<List<FoundLivre>> mAllFoundBooks;

    FoundBookRepository(Application application) {
        ProjectRoomDatabase db = ProjectRoomDatabase.getDatabase(application);
        mFoundLivreDao = db.foundLivreDao();
        mAllFoundBooks = mFoundLivreDao.getmAllFoundBooks();
    }

    LiveData<List<FoundLivre>> getmAllFoundBooks() {
        return mAllFoundBooks;
    }

    List<FoundLivre> getmAllFoundBooksList() {
        return mFoundLivreDao.getmAllFoundBooksList();
    }

    public void insert (FoundLivre book) {
        new FoundBookRepository.insertAsyncTask(mFoundLivreDao).execute(book);
    }

    public void delete (FoundLivre book) {
        new FoundBookRepository.deleteAsyncTask(mFoundLivreDao).execute(book);
    }

    public void deleteAll(){ mFoundLivreDao.deleteAll(); }

    public void updateBook(FoundLivre book){ mFoundLivreDao.updateFoundBook(book);}

    private static class insertAsyncTask extends AsyncTask<FoundLivre, Void, Void> {

        private FoundLivreDao mAsyncTaskDao;

        insertAsyncTask(FoundLivreDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FoundLivre... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<FoundLivre, Void, Void> {

        private FoundLivreDao mAsyncTaskDao;

        deleteAsyncTask(FoundLivreDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FoundLivre... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
