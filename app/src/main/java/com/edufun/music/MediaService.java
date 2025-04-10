package com.edufun.music;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.edufun.music.Activity.ActionPlaying;
import com.edufun.music.Activity.HomeActivity;

public class MediaService extends Service {
    private IBinder mBinder = new MyBinder();
    ActionPlaying actionPlaying;
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREVIOUS = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            String actionName = intent.getStringExtra("myActionName");
            switch (actionName){
                case ACTION_PLAY:
                    Toast.makeText(MediaService.this, "Play", Toast.LENGTH_SHORT).show();
                    if (actionPlaying!=null){
                        actionPlaying.playClicked();
                    }
                    // ((Activity)context).finish();
                    break;
                case ACTION_NEXT:
                    Toast.makeText(MediaService.this, "Next", Toast.LENGTH_SHORT).show();
                    if (actionPlaying!=null){
                        actionPlaying.nextClicked();
                    }

                    break;
                case ACTION_PREVIOUS:
                    Toast.makeText(MediaService.this,"Previous",Toast.LENGTH_SHORT).show();
                    if (actionPlaying!=null){
                        actionPlaying.previousClicked();
                    }
                    break;
            }
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class MyBinder extends Binder{
        public MediaService getService() {
            return MediaService.this;
        }
    }
    public void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }

}
