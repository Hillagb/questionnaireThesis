package com.example.questionnairethesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{
    TextView tvTitle;
    Button[] buttons;
    MediaPlayer player;
    ImageButton ibAudio;
    private ArrayList<StorageReference> segments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        int num= (int) (Math.random()*324);
        tvTitle=findViewById(R.id.tvTitle);

        ibAudio=findViewById(R.id.ibAudio);
        ibAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player = new MediaPlayer();

                String s="/gamliel"+num+".wav";

                Log.d("All Files", String.valueOf(storageRef.child("/").listAll()));
                storageRef.child(s).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            player.setDataSource(uri.toString());
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    player.start();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        tvTitle.setText("Failure");
                    }
                });

            }
        });
        buttons=new Button[3];
        buttons[0]=findViewById(R.id.natural);
        buttons[1]=findViewById(R.id.excitement);
        buttons[2]=findViewById(R.id.Anger);
        for (int i=0; i<buttons.length; i++){
            Button currentButton = buttons[i];
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}