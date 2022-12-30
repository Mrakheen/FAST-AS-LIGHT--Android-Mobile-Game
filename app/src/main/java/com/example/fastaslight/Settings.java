package com.example.fastaslight;

import static com.example.fastaslight.HomeMenu.database;
import static com.example.fastaslight.HomeMenu.key;
import static com.example.fastaslight.HomeMenu.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    private String newusername;
    private String newpassword;
    private String newpasswordre;
    TextView error, error1;
    private TextView newusernametext, newpasswordtext, newpasswordretext;
    private ImageButton Soundbutton, Soundbutton2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        newusernametext = findViewById(R.id.passwordsignup50);
        newpasswordtext = findViewById(R.id.passwordsignup500);
        newpasswordretext = findViewById(R.id.passwordsignup501);
        error = findViewById(R.id.errorPassTextsignup500);
        error1 = findViewById(R.id.errorPassTextsignup20);
        error.setVisibility(View.GONE);
        error1.setVisibility(View.GONE);
        findViewById(R.id.submitsignup50).setOnClickListener(this);
        Soundbutton = findViewById(R.id.soundButton);
        Soundbutton.setOnClickListener(this);
        Soundbutton2 = findViewById(R.id.muteButton);
        Soundbutton2.setOnClickListener(this);

        if(user.getAudio().equals("ON")){
            Soundbutton2.setVisibility(View.INVISIBLE);
            Soundbutton.setVisibility(View.VISIBLE);
            Soundbutton2.setEnabled(false);
            Soundbutton.setEnabled(true);
        }
        if(user.getAudio().equals("OF")){
            Soundbutton.setVisibility(View.INVISIBLE);
            Soundbutton2.setVisibility(View.VISIBLE);
            Soundbutton.setEnabled(false);
            Soundbutton2.setEnabled(true);
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton close_profile = findViewById(R.id.close_icon_profile999);
        close_profile.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.soundButton){
            HomeMenu.pauseMusic(null);
            user.setAudioOFF();
            Soundbutton.setVisibility(view.INVISIBLE);
            Soundbutton2.setVisibility(view.VISIBLE);
            Soundbutton.setEnabled(false);
            Soundbutton2.setEnabled(true);
        }
        if(view.getId() == R.id.muteButton){
            HomeMenu.playMusic(null);
            user.setAudioON();
            Soundbutton2.setVisibility(view.INVISIBLE);
            Soundbutton.setVisibility(view.VISIBLE);
            Soundbutton2.setEnabled(false);
            Soundbutton.setEnabled(true);
        }
        if (view.getId() == R.id.close_icon_profile999)
        {
            finish();
            //startActivity(new Intent(this, HomeMenu.class));
        }
        if (view.getId() == R.id.submitsignup50) {
            newusername = newusernametext.getText().toString();
            newpassword = newpasswordtext.getText().toString();
            newpasswordre = newpasswordretext.getText().toString();
            if (newusername.isEmpty() && (newpassword.isEmpty() || newpasswordre.isEmpty()))
            {
                error1.setVisibility(View.VISIBLE);

//               //both username and password
            }
            else
            {
                DatabaseReference users = database.child("User");
                if(newusername.isEmpty())
                {
                    if(newpassword.equals(newpasswordre))
                    {
                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot userDB : snapshot.getChildren()) {
                                    if (key.equals(userDB.getKey())) {
                                        Map<String, Object> updatePass = new HashMap<>();
                                        updatePass.put("User/" + key + "/password/", newpassword);
                                        database.updateChildren(updatePass);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                throw error.toException();
                            }
                        });
                    }
                    else
                    {
                        error.setVisibility(View.VISIBLE);
                    }
                }
//                no username
                else if(newpassword.isEmpty())
                {
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            for (DataSnapshot userDB : snapshot.getChildren())
                            {
                                if (key.equals(userDB.getKey())) {
                                    user.setUser_name(newusername);
                                    Map<String, Object> updatePass = new HashMap<>();
                                    updatePass.put("User/" + key + "/user_name/", newusername);
                                    database.updateChildren(updatePass);
                                    break;
                                }
                            }
                            DatabaseReference leaderboard = database.child("Leaderboard");
                            leaderboard.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot lDB: snapshot.getChildren())
                                    {
                                        if(key.equals(lDB.child("key").getValue())){
                                            HashMap<String, Object> lpass = new HashMap<>();
                                            lpass.put("Leaderboard/"+user.getRank()+"/user_name/",newusername);
                                            database.updateChildren(lpass);
                                            break;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });

                }
                else
                {
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot userDB : snapshot.getChildren()) {
                                if (key.equals(userDB.getKey())) {
                                    user.setUser_name(newusername);
                                    Map<String, Object> updatePass = new HashMap<>();
                                    updatePass.put("User/" + key + "/user_name/", newusername);
                                    updatePass.put("User/" + key + "/password/", newpassword);
                                    database.updateChildren(updatePass);
                                    break;
                                }
                            }
                            DatabaseReference leaderboard = database.child("Leaderboard");
                            leaderboard.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot lDB: snapshot.getChildren())
                                    {
                                        if(key.equals(lDB.child("key").getValue())){
                                            HashMap<String, Object> lpass = new HashMap<>();
                                            lpass.put("Leaderboard/"+user.getRank()+"/user_name/",newusername);
                                            database.updateChildren(lpass);
                                            break;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });

                }

            }
            finish();
            startActivity(new Intent(this, HomeMenu.class));

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginOrSignUp.class));
        finish();
    }

}