package edu.niu.cs.z1888485.Utils;
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
 * Class name: BootUpReceiver                                  *
 * Purpose: This class is used for intents purposes to start   *
 *          an activity.                                       *
 *                                                             *
 ***************************************************************/
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edu.niu.cs.z1888485.activities.MainActivity;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
