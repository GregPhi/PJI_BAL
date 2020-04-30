package com.example.projetlivre.dataB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.projetlivre.ProjectRoomDatabase;
import com.example.projetlivre.dao.BookDao;
import com.example.projetlivre.object.Book;

import java.util.List;

public class BookRepository{

    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    BookRepository(Application application) {
        ProjectRoomDatabase db = ProjectRoomDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getmAllBooks();
    }

    LiveData<List<Book>> getmAllBooks() {
        return mAllBooks;
    }

    LiveData<List<Book>> getAllBookForAMatiere(String m){ return mBookDao.getAllBookForAMatiere(m); }

    LiveData<List<Book>> findBookWithCodeBarre(String i) { return mBookDao.findBookWithCodeBarre(i); }

    public void insert (Book book) {
        new insertAsyncTask(mBookDao).execute(book);
    }

    public void delete (Book book) {
        new deleteAsyncTask(mBookDao).execute(book);
    }

    public void deleteAll(){ mBookDao.deleteAll(); }

    public void updateBook(Book book){ mBookDao.updateBook(book);}

    private static class insertAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao mAsyncTaskDao;

        insertAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Book... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao mAsyncTaskDao;

        deleteAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Book... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
