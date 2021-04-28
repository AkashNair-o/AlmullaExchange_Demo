package com.example.almullaexchange_demo.stopWatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almullaexchange_demo.MainActivity;
import com.example.almullaexchange_demo.R;
import com.example.almullaexchange_demo.Simple_Encryption_Decryption;
import com.example.almullaexchange_demo.changeLanguage.Change_Language;
import com.example.almullaexchange_demo.toDo.ToDo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ligl.android.widget.iosdialog.IOSDialog;

import java.util.Locale;

public class StopWatch extends AppCompatActivity
{
    TextView tvName, txtVersion;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    private DrawerLayout dl;
    BottomNavigationView bottomNavigationView;

    private int seconds = 0;
    private TextView txt_timer;
    private boolean isRunning=false;
    private ImageButton btn_paus;
    private ImageButton btn_stop;
    private ImageButton btn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        initviews();


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            tvName.setText(getResources().getString(R.string.welcome)+firebaseUser.getDisplayName());
        }

        googleSignInClient = GoogleSignIn.getClient(StopWatch.this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startCountdown();
            }
        });
        btn_paus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                seconds = 0;
            }
        });

        initTimmer();

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            String version = pInfo.versionName;
            txtVersion.setText("App Version: "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initviews()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        tvName = findViewById(R.id.txtName);
        dl = (DrawerLayout) findViewById(R.id.activity_drawer);

        bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.stopwatch);

        txt_timer = findViewById(R.id.txt_timer);
        txtVersion = findViewById(R.id.txtVersion);
        btn_paus= findViewById(R.id.btn_paus);
        btn_stop= findViewById(R.id.btn_stop);
        btn_play= findViewById(R.id.btn_play);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.encryption:
                        Intent intent = new Intent(getApplicationContext(), Simple_Encryption_Decryption.class);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.todo:
                        intent = new Intent(getApplicationContext(), ToDo.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.stopwatch:
                        return true;
                }
                return false;
            }
        });
    }

    public void logout(View view)
    {
        new IOSDialog.Builder(StopWatch.this)
                .setTitle(getResources().getString(R.string.delete))
            .setMessage(getResources().getString(R.string.are_you_sure))
            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                firebaseAuth.signOut();
                                Toast.makeText(StopWatch.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            })
            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            }).setCancelable(false).create().show();
    }

    public void changeLanguge(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Change_Language.class);
        startActivity(intent);
    }

    public void openNav(View view)
    {
        dl.openDrawer(Gravity.LEFT);
    }

    private void initTimmer()
    {
        final Handler handler = new Handler();
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d",
                        hours, minutes, secs);

                txt_timer.setText(time);
                if (isRunning)
                {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void startCountdown()
    {
        isRunning = true;
    }
}