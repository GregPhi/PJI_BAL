package com.example.projetlivre.dataB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.projetlivre.ProjectRoomDatabase;
import com.example.projetlivre.dao.LivreDao;
import com.example.projetlivre.object.Livre;

import java.util.List;

public class BookRepository{

    private LivreDao mLivreDao;
    private LiveData<List<Livre>> mAllBooks;

    BookRepository(Application application) {
        ProjectRoomDatabase db = ProjectRoomDatabase.getDatabase(application);
        mLivreDao = db.bookDao();
        mAllBooks = mLivreDao.getmAllBooks();
    }

    LiveData<List<Livre>> getmAllBooks() {
        return mAllBooks;
    }

    LiveData<List<Livre>> getAllBookForAMatiere(String m){ return mLivreDao.getAllBookForAMatiere(m); }

    LiveData<List<Livre>> findBookWithCodeBarre(String i) { return mLivreDao.findBookWithCodeBarre(i); }

    public void insert (Livre book) {
        new insertAsyncTask(mLivreDao).execute(book);
    }

    public void delete (Livre book) {
        new deleteAsyncTask(mLivreDao).execute(book);
    }

    public void deleteAll(){ mLivreDao.deleteAll(); }

    public void updateBook(Livre book){ mLivreDao.updateBook(book);}

    private static class insertAsyncTask extends AsyncTask<Livre, Void, Void> {

        private LivreDao mAsyncTaskDao;

        insertAsyncTask(LivreDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Livre... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Livre, Void, Void> {

        private LivreDao mAsyncTaskDao;

        deleteAsyncTask(LivreDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Livre... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
