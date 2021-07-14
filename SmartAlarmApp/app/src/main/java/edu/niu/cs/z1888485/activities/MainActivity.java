package edu.niu.cs.z1888485.activities;
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
 * Class name: MainActivity                                    *
 * Purpose: This class has the functionality of alarm clock    *
 * where user can get the options to create, delete alarms and *
 * see the main menu with list of alarms.                      *
 *                                                             *
 ***************************************************************/
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.niu.cs.z1888485.Adapter.AlarmAdapter;
import edu.niu.cs.z1888485.Models.Alarm;
import edu.niu.cs.z1888485.Models.AlarmViewModel;
import edu.niu.cs.z1888485.R;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    final String[] permissions = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.MANAGE_EXTERNAL_STORAGE};

    final AlarmAdapter adapter = new AlarmAdapter(this);;
    RecyclerView recyclerview;
    public static final int ADD_ALARM_REQUEST = 1;
    public static final int EDIT_ALARM_REQUEST = 2;
    private AlarmViewModel alarmViewModel;
    private int posAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.recyclerview);

        recyclerview.setAdapter(adapter);
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        alarmViewModel.getAllAlarm().observe(this, alarms -> adapter.submitList(alarms));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                alarmViewModel.delete(adapter.getAlarmAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Alarm deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerview);
        adapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            /***************************************************************
             *                                                             *
             * App name:  Smart Alarm App                                  *
             *                                                             *
             * Method name: onItemClick                                    *
             * Purpose: This function is used to start the activity        *
             * after creating the intent for clicking on items.            *
             *                                                             *
             ***************************************************************/
            @Override
            public void onItemClick(Alarm alarm, int pos)
            {
                Intent intent = new Intent(MainActivity.this, CreateAlarm.class);
                intent.putExtra(CreateAlarm.EXTRA_ID, alarm.getId());
                intent.putExtra(CreateAlarm.EXTRA_MESSAGE, alarm.getAlarmMessage());
                intent.putExtra(CreateAlarm.EXTRA_DATE, alarm.getAlarmDate());
                intent.putExtra(CreateAlarm.EXTRA_TIME, alarm.getAlarmTime());
                intent.putExtra(CreateAlarm.EXTRA_Active, alarm.isAlarmActive());
                intent.putExtra(CreateAlarm.EXTRA_VOICE_MESSAGE, alarm.getVoiceMessage());
                posAdapter=pos;
                startActivityForResult(intent, EDIT_ALARM_REQUEST);
            }
            /***************************************************************
             *                                                             *
             * App name:  Smart Alarm App                                  *
             *                                                             *
             * Method name: onSwitchClick                                  *
             * Purpose: This function is used to update the data after     *
             * alarm is created using alarm id.                            *
             *                                                             *
             ***************************************************************/
            @Override
            public void onSwitchClick(Alarm alarm, int pos, boolean isChecked)
            {
                int id = alarm.getId();
                String message =alarm.getAlarmMessage();
                String date = alarm.getAlarmDate();
                String time = alarm.getAlarmTime();
                String repeat = alarm.getAlarmRepeat();
                String voiceMessage = alarm.getVoiceMessage();
                boolean alarmActive = isChecked;

                Alarm upAlarm = new Alarm(message, date, time, voiceMessage,alarmActive,repeat);
                upAlarm.setId(id);
                alarmViewModel.update(upAlarm);

            }
        });



    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: onActivityResult                               *
     * Purpose: This function is used to get the activity results. *
     * It will toast message if alarm is created, deleted or       *
     * updated.                                                    *
     *                                                             *
     ***************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ALARM_REQUEST && resultCode == RESULT_OK) {
            String message = data.getStringExtra(CreateAlarm.EXTRA_MESSAGE);
            String date = data.getStringExtra(CreateAlarm.EXTRA_DATE);
            String time = data.getStringExtra(CreateAlarm.EXTRA_TIME);
            String repeat = data.getStringExtra(CreateAlarm.EXTRA_Repeat);
            String voiceMessage = data.getStringExtra(CreateAlarm.EXTRA_VOICE_MESSAGE);
            boolean alarmActive = data.getBooleanExtra(CreateAlarm.EXTRA_Active, false);

            Alarm alarm = new Alarm(message, date, time, voiceMessage,alarmActive,repeat);
            alarmViewModel.insert(alarm);
            Toast.makeText(this, "Alarm saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_ALARM_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(CreateAlarm.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Alarm can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String message = data.getStringExtra(CreateAlarm.EXTRA_MESSAGE);
            String date = data.getStringExtra(CreateAlarm.EXTRA_DATE);
            String time = data.getStringExtra(CreateAlarm.EXTRA_TIME);
            String repeat = data.getStringExtra(CreateAlarm.EXTRA_Repeat);
            String voiceMessage = data.getStringExtra(CreateAlarm.EXTRA_VOICE_MESSAGE);
            boolean alarmActive = data.getBooleanExtra(CreateAlarm.EXTRA_Active, false);

            Alarm alarm = new Alarm(message, date, time, voiceMessage,alarmActive,repeat);
            alarm.setId(id);
            alarmViewModel.update(alarm);

            Toast.makeText(this, "Alarm updated", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == RESULT_FIRST_USER && requestCode == EDIT_ALARM_REQUEST)
        {
            alarmViewModel.delete(adapter.getAlarmAt(posAdapter));
            Toast.makeText(MainActivity.this, "Alarm deleted", Toast.LENGTH_SHORT).show();
        }
        else {

            Toast.makeText(this, "Alarm not saved", Toast.LENGTH_SHORT).show();
        }


    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: onResume                                       *
     * Purpose: This function is used to resume the application.   *
     *                                                             *
     ***************************************************************/
    @Override
    protected void onResume() {
        super.onResume();

    }

    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: goToCreateEventActivity                        *
     * Purpose: This function is used to start the activity using  *
     * the created intent and it will go to next class CreateAlarm.*
     *                                                             *
     ***************************************************************/

    private void goToCreateEventActivity() {
        Intent intent = new Intent(MainActivity.this, CreateAlarm.class);
        startActivityForResult(intent, ADD_ALARM_REQUEST);
    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: onCreateOptionsMenu                            *
     * Purpose: This function is used to create the option menu.   *
     *                                                             *
     ***************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: onOptionsItemSelected                          *
     * Purpose: This function is used to store which item value is *
     * selected from all items and perform the activity assigned to*
     * that item.                                                  *
     *                                                             *
     ***************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.add_alarm:
                goToCreateEventActivity();
                break;

            case R.id.delete_all_alarms:
                alarmViewModel.deleteAllNotes();
                Toast.makeText(this, "All Alarm deleted", Toast.LENGTH_SHORT).show();
                return true;
        }

        return true;
    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: onStart                                        *
     * Purpose: This function is used to start the application. It *
     * will check for permission.                                  *
     *                                                             *
     ***************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        askForPermission( 100);
    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: askForPermission                               *
     * Purpose: This function is to check the permissions for the  *
     * application to start.                                       *
     *                                                             *
     ***************************************************************/
    private void askForPermission(Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Arrays.toString(permissions)) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, requestCode);

        }

    }
}