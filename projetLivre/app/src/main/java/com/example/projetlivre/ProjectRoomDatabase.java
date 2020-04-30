package com.example.projetlivre;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projetlivre.dao.BookDao;
import com.example.projetlivre.object.Book;

@Database(entities = {Book.class},version = 2)
public abstract class ProjectRoomDatabase extends RoomDatabase {
    public abstract BookDao bookDao();

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

        private final BookDao mDao;

        PopulateDbAsync(ProjectRoomDatabase db) {
            mDao = db.bookDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            return null;
        }
    }
}
