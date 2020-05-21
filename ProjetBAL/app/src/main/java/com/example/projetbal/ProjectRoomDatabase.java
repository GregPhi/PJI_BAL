package com.example.projetbal;

import android.content.Context;
import android.os.AsyncTask;

import com.example.projetbal.dao.LivreDao;
import com.example.projetbal.object.EtatsLivre;
import com.example.projetbal.object.Livre;
import com.example.projetbal.object.StatutsLivre;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Livre.class},version = 1)
public abstract class ProjectRoomDatabase extends RoomDatabase {
    public abstract LivreDao bookDao();

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

        private final LivreDao mDao;

        PopulateDbAsync(ProjectRoomDatabase db) {
            mDao = db.bookDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Livre livre1 = new Livre();
            livre1.setCode_barre("9782013235327");
            livre1.setTitle("Hobbit");
            livre1.setEtats(EtatsLivre.NEUF.toString());
            livre1.setStatuts(StatutsLivre.PREPARE.toString());
            Livre livre2 = new Livre();
            livre2.setCode_barre("9782290019436");
            livre2.setTitle("GOT");
            livre2.setEtats(EtatsLivre.BONETAT.toString());
            livre2.setStatuts(StatutsLivre.PREPARE.toString());
            mDao.insert(livre1);
            mDao.insert(livre2);
            return null;
        }
    }
}
