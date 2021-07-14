package edu.niu.cs.z1888485.Models;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AlarmViewModel extends AndroidViewModel {
    private ModelRepository repository;
    private LiveData<List<Alarm>> allAlarm;
    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repository = new ModelRepository(application);
        allAlarm = repository.getAllAlarm();
    }
    public void insert(Alarm alarm) {
        repository.insert(alarm);
    }
    public void update(Alarm alarm) {
        repository.update(alarm);
    }
    public void delete(Alarm alarm) {
        repository.delete(alarm);
    }
    public void deleteAllNotes() {
        repository.deleteAllAlarm();
    }
    public LiveData<List<Alarm>> getAllAlarm() {
        return allAlarm;
    }
}