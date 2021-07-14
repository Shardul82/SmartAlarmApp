package edu.niu.cs.z1888485.Models;
import android.app.Application;

import android.os.AsyncTask;

import edu.niu.cs.z1888485.Database.AlarmDao;
import edu.niu.cs.z1888485.Database.AlarmDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ModelRepository
{
        private AlarmDao alarmDao;
        private LiveData<List<Alarm>> allNotes;

        public ModelRepository(Application application) {
            AlarmDatabase database = AlarmDatabase.getInstance(application);
            alarmDao = database.noteDao();
            allNotes = alarmDao.getAllAlarms();
        }
        public void insert(Alarm alarm) {
            new InsertAlarmAsyncTask(alarmDao).execute(alarm);
        }
        public void update(Alarm alarm) {
            new UpdateAlarmAsyncTask(alarmDao).execute(alarm);
        }
        public void delete(Alarm alarm) {
            new DeleteAlarmAsyncTask(alarmDao).execute(alarm);
        }
        public void deleteAllAlarm() {
            new DeleteAllAlarmAsyncTask(alarmDao).execute();
        }
        public LiveData<List<Alarm>> getAllAlarm() {
            return allNotes;
        }
        private static class InsertAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
            private AlarmDao alarmDao1;
            private InsertAlarmAsyncTask(AlarmDao alarmDao1) {
                this.alarmDao1 = alarmDao1;
            }
            @Override
            protected Void doInBackground(Alarm... alarms) {
                alarmDao1.insert(alarms[0]);
                return null;
            }
        }
        private static class UpdateAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
            private AlarmDao alarmDao1;
            private UpdateAlarmAsyncTask(AlarmDao alarmDao1) {
                this.alarmDao1 = alarmDao1;
            }
            @Override
            protected Void doInBackground(Alarm... alarms) {
                alarmDao1.update(alarms[0]);
                return null;
            }
        }
        private static class DeleteAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
            private AlarmDao noteDao;
            private DeleteAlarmAsyncTask(AlarmDao noteDao) {
                this.noteDao = noteDao;
            }
            @Override
            protected Void doInBackground(Alarm... alarms) {
                noteDao.delete(alarms[0]);
                return null;
            }
        }
        private static class DeleteAllAlarmAsyncTask extends AsyncTask<Void, Void, Void> {
            private AlarmDao alarmDao1;
            private DeleteAllAlarmAsyncTask(AlarmDao alarmDao1) {
                this.alarmDao1 = alarmDao1;
            }
            @Override
            protected Void doInBackground(Void... voids) {
                alarmDao1.deleteAllAlarm();
                return null;
            }
        }
    }

