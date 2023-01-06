package com.example.questionnairethesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class QuestionActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{
    TextView tvTitle;
    Button[] buttons;
    MediaPlayer player;
    ImageButton ibAudio;
    String s;
    private ArrayList<StorageReference> segments;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        db=FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference listRef=storage.getReference();
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                Log.d("Speeches", listRef.getName());
                for (StorageReference prefix: listResult.getPrefixes()){
                    Log.d("One Speech", prefix.getName());
                }
            }
        });
        int num= (int) (Math.random()*324);
        tvTitle=findViewById(R.id.tvTitle);

        ibAudio=findViewById(R.id.ibAudio);
        ibAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player = new MediaPlayer();

                s="/gamliel"+num+".wav";


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
                    CollectionReference tags=db.collection("tags");
                    Segment segment=new Segment(s,currentButton.getText().toString());
                    tags.add(segment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent intent=new Intent(QuestionActivity.this, QuestionActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
    }




    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}