package com.edufun.music;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.edufun.music.Activity.HomeActivity;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREVIOUS = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MediaService.class);
        if (intent!=null){
            switch (intent.getAction()){
                case ACTION_PLAY:
                   // Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                   // ((Activity)context).finish();
                    break;
                case ACTION_NEXT:
                 //   Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show();
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_PREVIOUS:
                 //   Toast.makeText(context,"Previous",Toast.LENGTH_SHORT).show();
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;
            }
        }
    }
}
