package com.edufun.music.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edufun.music.Adapter.SongAdapter;
import com.edufun.music.MediaService;
import com.edufun.music.Model.InternalSongModel;
import com.edufun.music.MyMediaPlayer;
import com.edufun.music.NotificationReceiver;
import com.edufun.music.R;
import com.edufun.music.databinding.ActivityHomeBinding;
import com.edufun.music.databinding.ActivityPlaySongBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlaySongActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {
    ActivityPlaySongBinding binding ;
    MediaService mediaService;
    ArrayList<InternalSongModel> songsList;
    InternalSongModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    String fromActivity;
    String title;
    int x =0;
    SharedPreferences shp;


    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREVIOUS = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    MediaSessionCompat mediaSession;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaySongBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.tvTitle.setSelected(true);

        songsList = (ArrayList<InternalSongModel>) getIntent().getSerializableExtra("LIST");
        fromActivity = getIntent().getStringExtra("fromActivity");
        title = getIntent().getStringExtra("title");

        mediaSession = new MediaSessionCompat(this,"PlayerAudio");

        setResourceWithMusic();

        binding.imgPlay.setOnClickListener(v -> {
            pausePlay();
        });
        binding.imgPreviousSong.setOnClickListener(v -> {
            playPreviousSong();
        });
        binding.imgNextSong.setOnClickListener(v -> {
            playNextSong();
        });

        PlaySongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    binding.tvStartTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if (mediaPlayer.isPlaying()){
                        binding.imgPlay.setImageResource(R.drawable.pause);
                        binding.imgSongIcon.setRotation(x++);
                    }else {
                        binding.imgPlay.setImageResource(R.drawable.pause_icon);
                        binding.imgSongIcon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            String start = convertToMMSS(String.valueOf(mediaPlayer.getCurrentPosition()));
            String end = convertToMMSS(String.valueOf(mediaPlayer.getDuration()));

            if (start.equalsIgnoreCase(end) && !start.equalsIgnoreCase("00:00")){
                playNextSong();
            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    void setResourceWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        shp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString("fromActivity","songActivity");
        editor.putInt("currentSong",MyMediaPlayer.currentIndex);
        editor.apply();
        binding.tvTitle.setText(currentSong.getTitle());
        binding.tvEndTime.setText(convertToMMSS(currentSong.getDuration()));
        playMusic();
    }
    private void playMusic(){
        if (title != null) {
            if (title.equalsIgnoreCase(songsList.get(MyMediaPlayer.currentIndex).getTitle())){
                title="null!@!#$%$#@@##~!)_(*&^%$$$######@";
                    binding.seekBar.setProgress(0);
                    binding.seekBar.setMax(mediaPlayer.getDuration());

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(songsList.get(MyMediaPlayer.currentIndex).getPath());
                    byte[] art = retriever.getEmbeddedPicture();
                    if (art != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                        binding.imgSongIcon.setImageBitmap(bitmap);
                    }
                    if (!mediaPlayer.isPlaying()) {
                        setMediaPlayer();
                    }
            }else {
               setMediaPlayer();
            }
        }else {
           setMediaPlayer();
        }
    }
    private void setMediaPlayer(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            showNotification(R.drawable.pause);
//            mediaPlayer.setOnCompletionListener(mp -> {
//                playNextSong();
//            });
            binding.seekBar.setProgress(0);
            binding.seekBar.setMax(mediaPlayer.getDuration());
            //Picasso.get().load(Uri.parse(songsList.get(MyMediaPlayer.currentIndex).getImageIcon())).placeholder(R.drawable.musical_icon).into(binding.imgSongIcon);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(songsList.get(MyMediaPlayer.currentIndex).getPath());
            byte[] art = retriever.getEmbeddedPicture();
            if (art != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                binding.imgSongIcon.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void playNextSong(){
        if (MyMediaPlayer.currentIndex==songsList.size()-1) {
            MyMediaPlayer.currentIndex =0;
            mediaPlayer.reset();
            setResourceWithMusic();
            return;
        }
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourceWithMusic();
    }
    private void playPreviousSong(){
        if (MyMediaPlayer.currentIndex==0) {
            MyMediaPlayer.currentIndex =songsList.size()-1;
            mediaPlayer.reset();
            setResourceWithMusic();
            return;
        }
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourceWithMusic();
    }
    private void pausePlay(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            showNotification(R.drawable.pause_icon);
        }else {
            mediaPlayer.start();
            showNotification(R.drawable.pause);
        }
    }
    @SuppressLint("DefaultLocale")
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(PlaySongActivity.this, HomeActivity.class);
        in.putExtra("LIST",songsList);
        in.putExtra("fromActivity", "songActivity");
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    @Override
    public void nextClicked() {
        playNextSong();
    }

    @Override
    public void previousClicked() {
        playPreviousSong();
    }

    @Override
    public void playClicked() {
        pausePlay();
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MediaService.MyBinder binder = (MediaService.MyBinder) service;
        mediaService = binder.getService();
        mediaService.setCallBack(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mediaService=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,MediaService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
    }
    private void showNotification(int playPauseBtn){
        try {
            Intent intent = new Intent(this, HomeActivity.class);
            Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
            Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
            Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
            PendingIntent pendingIntent;
            PendingIntent nextSongIntent;
            PendingIntent previousSongIntent;
            PendingIntent playPauseSongIntent;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                nextSongIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_MUTABLE);
                previousSongIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_MUTABLE);
                playPauseSongIntent = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_MUTABLE);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            }else {
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                nextSongIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                previousSongIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                playPauseSongIntent = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Notification notification = new NotificationCompat.Builder(this,"OfflineSong")
                    .setSmallIcon(R.drawable.musical_icon)
                    .setLargeIcon(songIcon())
                    .setContentTitle(songsList.get(MyMediaPlayer.currentIndex).getTitle())
                    .addAction(R.drawable.back,ACTION_PREVIOUS,previousSongIntent)
                    .addAction(playPauseBtn,ACTION_PLAY,playPauseSongIntent)
                    .addAction(R.drawable.next,ACTION_NEXT,nextSongIntent)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mediaSession.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setChannelId("OfflineSong")
                    .setOnlyAlertOnce(true)
                    .build();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("OfflineSong","ActionNotification",NotificationManager.IMPORTANCE_HIGH));
            }
            notificationManager.notify(10,notification);
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private Bitmap songIcon (){
        //currentSong = list.get(MyMediaPlayer.currentIndex);
        //binding.tvTitle.setText(currentSong.getTitle());

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(songsList.get(MyMediaPlayer.currentIndex).getPath());
        byte[] art = retriever.getEmbeddedPicture();
        if (art!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
            //binding.songImage.setImageBitmap(bitmap);
            return bitmap;
        }else return null;

    }


}