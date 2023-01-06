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
import java.util.Random;

public class QuestionActivity extends AppCompatActivity{
    TextView tvTitle;
    Button[] buttons;
    MediaPlayer player;
    ImageButton ibAudio;
    int filesCounter;
    String segmentName;
    ArrayList<String>Uris;
    private ArrayList<StorageReference> segments;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        db=FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef=storage.getReference();
        Uris=new ArrayList<>();
        tvTitle=findViewById(R.id.tvTitle);
        ibAudio=findViewById(R.id.ibAudio);
        ibAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player = new MediaPlayer();
                listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {
                                filesCounter=0;
                                for (StorageReference item : listResult.getItems()) {
                                    String temp="https://firebasestorage.googleapis.com/v0/b/questionnairethesis-8ee25.appspot.com/o/ExampleForSegmentName.wav?alt=media&token=e9cb059b-4eea-4af6-b8f0-16de3afd59a8";
                                    segmentName=item.toString().substring(item.toString().lastIndexOf("/")+1,item.toString().lastIndexOf("."));
                                    temp=temp.replace("ExampleForSegmentName",segmentName);
                                    if (temp!=null)
                                        Uris.add(temp);
                                    filesCounter++;
                                }
                                Random random=new Random();
                                int randIndex=random.nextInt(filesCounter);
                                    try {
                                        String currURI=Uris.get(randIndex);
                                        segmentName=currURI.substring(0,currURI.lastIndexOf(".wav"));
                                        segmentName=segmentName.substring(segmentName.lastIndexOf("/")+1);
                                        Log.d("segmentName",segmentName);
                                        player.setDataSource(currURI);
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
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Uh-oh, an error occurred!
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
                    Segment segment=new Segment(segmentName,currentButton.getText().toString());
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

}