package com.example.projetbal.dataB.matiere;

import android.app.Application;
import android.os.AsyncTask;

import com.example.projetbal.ProjectRoomDatabase;
import com.example.projetbal.dao.LivreDao;
import com.example.projetbal.dao.MatiereDao;
import com.example.projetbal.dataB.book.BookRepository;
import com.example.projetbal.object.book.Livre;
import com.example.projetbal.object.book.Matiere;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MatiereRepository {
    private MatiereDao matiereDao;
    private LiveData<List<Matiere>> mAllMatieres;

    MatiereRepository(Application application) {
        ProjectRoomDatabase db = ProjectRoomDatabase.getDatabase(application);
        matiereDao = db.matiereDao();
        mAllMatieres = matiereDao.getmAllMatieres();
    }

    LiveData<List<Matiere>> getmAllMatieres() {
        return mAllMatieres;
    }

    List<Matiere> getmAllMatieresForSpinner(){ return matiereDao.getmAllMatieresForSpinner(); }

    public void insert (Matiere book) {
        new MatiereRepository.insertAsyncTask(matiereDao).execute(book);
    }

    public void delete (Matiere book) {
        new MatiereRepository.deleteAsyncTask(matiereDao).execute(book);
    }

    public void deleteAll(){ matiereDao.deleteAll(); }

    public void updateBook(Matiere book){ matiereDao.updateBook(book);}

    private static class insertAsyncTask extends AsyncTask<Matiere, Void, Void> {

        private MatiereDao mAsyncTaskDao;

        insertAsyncTask(MatiereDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Matiere... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Matiere, Void, Void> {

        private MatiereDao mAsyncTaskDao;

        deleteAsyncTask(MatiereDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Matiere... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
