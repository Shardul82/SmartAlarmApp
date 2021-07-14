package edu.niu.cs.z1888485.Database;
/***************************************************************
 *                                                             *
 * CSCI 524      Graduate Semester Project         Spring 2021 *
 *                                                             *
 *                                                             *
 * Programmer:    Shardul Deepak Arjunwadkar(z1888485)         *
 *                Ashwanth Kalaiselvi anandhan(z1886742),      *
 *                                                             *
 *                                                             *
 * Due Date & Time:   04/22/2021 11:59PM                       *
 *                                                             *
 * Class name: AlarmDatabase                                   *
 * Purpose: This abstract class is used for creating the       *
 * database table and perform the tasks related to it.         *
 *                                                             *
 ***************************************************************/
import android.content.Context;
import android.os.AsyncTask;

import edu.niu.cs.z1888485.Models.Alarm;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
@Database(entities = {Alarm.class}, version = 3)
public abstract class AlarmDatabase extends RoomDatabase
{

        private static AlarmDatabase instance;
        public abstract AlarmDao noteDao();
        public static synchronized AlarmDatabase getInstance(Context context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AlarmDatabase.class, "alarm_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
            return instance;
        }
        private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateDbAsyncTask(instance).execute();
            }
        };
        private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
            private AlarmDao eventDao;
            private PopulateDbAsyncTask(AlarmDatabase db) {
                eventDao = db.noteDao();
            }
            @Override
            protected Void doInBackground(Void... voids) {
                eventDao.insert(new Alarm("Alarm test", "14/7/2021","7:20PM","",false , "Once only"));

                return null;
            }
        }
    }

