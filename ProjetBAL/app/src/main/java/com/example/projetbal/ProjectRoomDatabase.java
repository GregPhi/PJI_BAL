package com.example.projetbal;

import android.content.Context;
import android.os.AsyncTask;

import com.example.projetbal.dao.FoundLivreDao;
import com.example.projetbal.dao.LivreDao;
import com.example.projetbal.dao.MatiereDao;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Matiere;
import com.example.projetbal.object.book.enumO.EtatsLivre;
import com.example.projetbal.object.book.Livre;
import com.example.projetbal.object.book.enumO.StatutsLivre;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Livre.class, FoundLivre.class, Matiere.class},version = 5)
public abstract class ProjectRoomDatabase extends RoomDatabase {
    public abstract LivreDao bookDao();
    public abstract FoundLivreDao foundLivreDao();
    public abstract MatiereDao matiereDao();

    private static volatile ProjectRoomDatabase INSTANCE;

    public static ProjectRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProjectRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProjectRoomDatabase.class, "project_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LivreDao mBookDao;
        private final FoundLivreDao mFoundBookDao;
        private final MatiereDao matiereDao;

        PopulateDbAsync(ProjectRoomDatabase db) {
            mBookDao = db.bookDao();
            mFoundBookDao = db.foundLivreDao();
            matiereDao = db.matiereDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mBookDao.deleteAll();
            mFoundBookDao.deleteAll();
            matiereDao.deleteAll();

            return null;
        }
    }
}
