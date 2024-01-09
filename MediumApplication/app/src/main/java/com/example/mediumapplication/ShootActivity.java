package com.example.mediumapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import java.util.Arrays;
import java.util.List;

public class ShootActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    int sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);

        Spinner spinner = findViewById(R.id.spinner);

        List<String> guns = Arrays.asList("beretta", "ak47", "silhouette");
        ImageView selectedGunImage = findViewById(R.id.imageView);

        SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), guns);
        adapter.setDropDownViewResource(R.layout.dropdown);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGun = guns.get(position);
                selectedGunImage.setImageResource(getDrawableResource(selectedGun));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }
    private int getDrawableResource(String gunName) {
        int resourceId = 0;
        if (gunName.equals("beretta")) {
            resourceId = R.drawable.beretta;
            mediaPlayer = MediaPlayer.create(this, R.raw.p2000);
            sound = R.raw.p2000;
        } else if (gunName.equals("ak47")) {
            resourceId = R.drawable.ak47;
            mediaPlayer = MediaPlayer.create(this, R.raw.ak);
            sound = R.raw.ak;
        }else if(gunName.equals("silhouette")){
            resourceId = R.drawable.silhouette;
            mediaPlayer = MediaPlayer.create(this, R.raw.awp);
            sound = R.raw.awp;
        }
        return resourceId;
    }

    public void fire(View v){
        ImageView imageView = findViewById(R.id.imageView);
        Animation tiltAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imageView.startAnimation(tiltAnimation);
        playSound(sound);
    }

    private void playSound(int soundResource) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResource);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
}