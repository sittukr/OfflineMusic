package com.edufun.music;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvHello;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvHello= findViewById(R.id.helloWorld);

        getData();
    }
    public void getData(){

        //.url("https://spotify23.p.rapidapi.com/search/?q=desi%20kalakar&type=multi&offset=0&limit=10&numberOfTopResults=5")
        RetrofitClient.getInstance().searchApiData().searchApiData("Love","multi")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()){
                            JsonObject jsonObject = response.body();

                            JsonObject albumsOb = jsonObject.getAsJsonObject("albums");
                            JsonArray itemArray = albumsOb.getAsJsonArray("items").getAsJsonArray();
                            for(JsonElement itemElement : itemArray){
                                JsonObject dataOb = itemElement.getAsJsonObject();
                                JsonObject uriOb = dataOb.get("data").getAsJsonObject();
                                String uriString = uriOb.get("uri").getAsString();
                                String nameString = uriOb.get("name").getAsString();
                                JsonObject artistsOb = uriOb.get("artists").getAsJsonObject();
                                JsonArray itemsObject = artistsOb.get("items").getAsJsonArray();
                                for (JsonElement dataElement : itemsObject){
                                    JsonObject dataObject = dataElement.getAsJsonObject();
                                    String uri_String = dataObject.get("uri").getAsString();
                                    JsonObject profileOb = dataObject.get("profile").getAsJsonObject();
                                    String nameOb = profileOb.get("name").getAsString();
                                }
                                JsonObject coverArtOb = uriOb.get("coverArt").getAsJsonObject();
                                JsonArray sourcesArray = coverArtOb.get("sources").getAsJsonArray();
                                for (JsonElement element : sourcesArray){
                                    JsonObject object = element.getAsJsonObject();
                                    String urlArt = object.get("url").getAsString();
                                    String width = object.get("width").getAsString();
                                    String height = object.get("height").getAsString();
                                }
                                JsonObject dateOb = uriOb.get("date").getAsJsonObject();
                                String year = dateOb.get("year").getAsString();
                                tvHello.setText(year.toString());

                            }




                        }else Toast.makeText(MainActivity.this, "response error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable throwable) {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}