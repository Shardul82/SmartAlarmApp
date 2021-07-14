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
 * Class name: CreateAlarm                                     *
 * Purpose: This class has the functionality of alarm clock    *
 * like set date, set time, audio message and delete alarm.    *
 *                                                             *
 ***************************************************************/
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;


import edu.niu.cs.z1888485.Models.Alarm;
import edu.niu.cs.z1888485.Models.AlarmViewModel;
import edu.niu.cs.z1888485.Utils.AlarmBrodcast;
import edu.niu.cs.z1888485.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

public class CreateAlarm extends AppCompatActivity  {
    private static final String APP_FOLDER_NAME = "VoiceMessages";
    public static final String EXTRA_ID = "edu.niu.cs.z1888485.EXTRA_ID";
    public static final String EXTRA_TIME = "edu.niu.cs.z1888485.EXTRA_TIME";
    public static final String EXTRA_Repeat = "edu.niu.cs.z1888485.EXTRA_Repeat";
    public static final String EXTRA_MESSAGE = "edu.niu.cs.z1888485.EXTRA_Message";
    public static final String EXTRA_DATE = "edu.niu.cs.z1888485.EXTRA_DATE";
    public static final String EXTRA_VOICE_MESSAGE = "edu.niu.cs.z1888485.EXTRA_VOICE_MESSAGE";
    public static final String EXTRA_Active = "edu.niu.cs.z1888485.EXTRA_Active";

    Button   btn_done , btn_delete;
    ImageView btn_record;
    TextView tv_time , tv_date;
    EditText et_message;
    String timeTonotify ;
    TextToSpeech textToSpeech;
    private String chooseSpinner;
    private Spinner spinnerRepeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
         init();


        findViewById(R.id.btn_speech).setOnClickListener(v -> {
            Toast.makeText(CreateAlarm.this, et_message.getText(),Toast.LENGTH_SHORT).show();
            textToSpeech.speak(et_message.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        });

        btn_record.setOnClickListener(v -> recordSpeech());
        btn_done.setOnClickListener(v -> submit());
        btn_delete.setOnClickListener(v -> deleteAlarm());
        findViewById(R.id.ll_time).setOnClickListener(v -> { selectTime(); });
        findViewById(R.id.ll_date).setOnClickListener(v -> selectDate());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> onBackPressed());

    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: deleteAlarm                                    *
     * Purpose: This function is used to delete the alarm.         *
     *                                                             *
     ***************************************************************/
    private void deleteAlarm()
    {

        Intent data = new Intent();
        setResult(RESULT_FIRST_USER, data);
        finish();

    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: init                                           *
     * Purpose: This function is used to initiate the items used   *
     * in the application to create an alarm. Intent is created.   *
     *                                                             *
     ***************************************************************/
    private void init()
    {
        Intent intent = getIntent();
        btn_record = findViewById(R.id.btn_record);
        tv_time = findViewById(R.id.et_time);
        tv_date = findViewById(R.id.tv_date);
        btn_done = findViewById(R.id.btn_save);
        btn_delete = findViewById(R.id.btn_delete);
        et_message = findViewById(R.id.editext_message);
        spinnerRepeat= findViewById(R.id.spinner_repeat);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerRepeat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(adapter);
        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                chooseSpinner= parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (intent.hasExtra(EXTRA_ID))
        {
            getSupportActionBar().setTitle("Edit Alarm");
            btn_delete.setVisibility(View.VISIBLE);
            tv_time.setText(intent.getStringExtra(EXTRA_TIME));
            tv_date.setText(intent.getStringExtra(EXTRA_DATE));
            et_message.setText(intent.getStringExtra(EXTRA_MESSAGE));

        }

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        textToSpeech=new TextToSpeech(getApplicationContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: submit                                         *
     * Purpose: This function is used for the description to be    *
     * mentioned in the alarm. This information will be used for   *
     * voice notifications of the alarm.                           *
     *                                                             *
     ***************************************************************/
    private void submit() {
        String message = et_message.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Please Enter or record the text", Toast.LENGTH_SHORT).show();
        } else {
            if (tv_time.getText().toString().equals("Select Time") || tv_date.getText().toString().equals("Select date")) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            } else
            {

                HashMap<String, String> myHashRender = new HashMap<String, String>();
                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, message);




                String exStoragePath = getExternalFilesDir("sound").getAbsolutePath();

                File appTmpPath = new File(exStoragePath + "/"+getRandomString(12) +".mp3");

                String destFileName = appTmpPath.getPath();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.synthesizeToFile(message,null, appTmpPath, "test");
                      Toast.makeText(CreateAlarm.this, destFileName, Toast.LENGTH_SHORT).show();
                }

                String value = (message);
                String date = (tv_date.getText().toString().trim());
                String time = (tv_time.getText().toString().trim());

                Intent data = new Intent();
                data.putExtra(EXTRA_TIME, time);
                data.putExtra(EXTRA_MESSAGE, value);
                data.putExtra(EXTRA_DATE, date);
                data.putExtra(EXTRA_Active, true);
                data.putExtra(EXTRA_Repeat, chooseSpinner);
                data.putExtra(EXTRA_VOICE_MESSAGE, destFileName);
                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ID, id);
                }
               setAlarm(id,value, date, time,destFileName,chooseSpinner);
                setResult(RESULT_OK, data);
                finish();
            }
            }

    }


    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: selectTime                                     *
     * Purpose: This function is used for the selection of time    *
     * of an alarm. The clock is in 24 hour format.                *
     *                                                             *
     ***************************************************************/
    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            timeTonotify = i + ":" + i1;
            tv_time.setText(FormatTime(i, i1));
        }, hour, minute, false);
        timePickerDialog.show();

    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: selectDate                                     *
     * Purpose: This function is used for the selection of date    *
     * of an alarm.                                                *
     *                                                             *
     ***************************************************************/
    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tv_date.setText((month + 1)  + "/" + day + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: FormatTime                                     *
     * Purpose: This function is used define the time format.      *
     *                                                             *
     ***************************************************************/
    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: recordSpeech                                   *
     * Purpose: This function is used get the voice record which is*
     * description mentioned for the alarm. It will read message   *
     * stored in the database.                                     *
     *                                                             *
     ***************************************************************/
    private void recordSpeech() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {

            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "Your device does not support Speech recognizer", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                et_message.setText(text.get(0));
            }
        }

    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: setAlarm                                       *
     * Purpose: This function is used to set alarm. This function  *
     * is used for the selection of alarm repeat. User can select  *
     * daily, monthly or once options for the alarm.               *
     *                                                             *
     ***************************************************************/
    private void setAlarm(int id ,String text, String date, String time , String path, String repeat) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("message", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("VoiceMessage", path);
        intent.putExtra("id", id);

        if (repeat.equals("Daily"))
        {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String dateandtime = date + " " + timeTonotify;
            DateFormat formatter = new SimpleDateFormat("M/d/yyyy hh:mm");
            Toast.makeText(this, "Daily", Toast.LENGTH_SHORT).show();
            try {
                Date date1 = formatter.parse(dateandtime);
                am.setRepeating(AlarmManager.RTC_WAKEUP, date1.getTime(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (repeat.equals("monthly"))
        {

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String dateandtime = date + " " + timeTonotify;
            DateFormat formatter = new SimpleDateFormat("M/d/yyyy hh:mm");
            try {
                Date date1 = formatter.parse(dateandtime);
                am.setRepeating(AlarmManager.RTC_WAKEUP, date1.getTime(),
                        AlarmManager.INTERVAL_DAY*30, pendingIntent);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "once only", Toast.LENGTH_SHORT).show();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String dateandtime = date + " " + timeTonotify;
            DateFormat formatter = new SimpleDateFormat("M/d/yyyy hh:mm");


            try {
                Date date1 = formatter.parse(dateandtime);
                am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    /***************************************************************
     *                                                             *
     * App name:  Smart Alarm App                                  *
     *                                                             *
     * Method name: getRandomString                                *
     * Purpose: This function is used to get the random string.    *
     * Also it is used to store the path of the message.           *
     *                                                             *
     ***************************************************************/
    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(random.nextInt(9));
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
