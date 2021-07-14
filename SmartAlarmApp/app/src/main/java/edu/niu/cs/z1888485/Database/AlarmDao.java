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
 * Class name: AlarmDao                                        *
 * Purpose: This interface is used to define the queries for   *
 * database.                                                   *
 *                                                             *
 ***************************************************************/
import edu.niu.cs.z1888485.Models.Alarm;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AlarmDao {

    @Delete
    void delete(Alarm alarm );

    @Update
    void update(Alarm alarm);

    @Query("SELECT * FROM myAlarm")
    LiveData<List<Alarm>> getAllAlarms();

    @Query("DELETE FROM myAlarm")
    void deleteAllAlarm();

    @Insert
    void insert(Alarm alarm);


}
