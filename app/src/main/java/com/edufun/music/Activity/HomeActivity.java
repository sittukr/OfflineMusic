package com.edufun.music.Activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.edufun.music.Adapter.SongAdapter;
import com.edufun.music.MainActivity;
import com.edufun.music.MediaService;
import com.edufun.music.Model.InternalSongModel;
import com.edufun.music.MyMediaPlayer;
import com.edufun.music.R;
import com.edufun.music.databinding.ActivityHomeBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    ArrayList<InternalSongModel> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (checkPermission() == false){
            requestPermission();
            return;
        }

        loadSongs();

//        SharedPreferences shp = getPreferences(MODE_PRIVATE);
//        String fromActivity= shp.getString("fromActivity",null);
//        int index = shp.getInt("currentSong" ,1);
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();



//        retriever.setDataSource(songList.get(index).getPath());
//        byte[] art = retriever.getEmbeddedPicture();
//        if (art != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
//            binding.imgSongIcon.setImageBitmap(bitmap);
//        }


    }
    boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
    void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 1);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSongs(); // Retry loading songs after permission granted
        } else {
            Toast.makeText(this, "Permission denied. Can't load songs.", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadSongs() {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String path = cursor.getString(1);
                String duration = cursor.getString(2);
                String albumId = cursor.getString(3);

                File file = new File(path);
                if (file.exists()) {
                    songList.add(new InternalSongModel(path, title, duration, albumId));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (songList.isEmpty()) {
            binding.tvNoSong.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            SongAdapter adapter = new SongAdapter(songList, this);
            binding.recyclerView.setAdapter(adapter);
        }
    }
}