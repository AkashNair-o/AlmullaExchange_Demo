package com.example.almullaexchange_demo.toDo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almullaexchange_demo.MainActivity;
import com.example.almullaexchange_demo.R;
import com.example.almullaexchange_demo.Simple_Encryption_Decryption;
import com.example.almullaexchange_demo.changeLanguage.Change_Language;
import com.example.almullaexchange_demo.stopWatch.StopWatch;
import com.example.almullaexchange_demo.toDo.todoAdd.Todo_Act;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDo extends AppCompatActivity
{
    TextView tvName;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    private DrawerLayout dl;
    BottomNavigationView bottomNavigationView;

    private SwipeableRecyclerView rv_List;
    private TextView txtView;
    DatabaseHelper showTask;
    List<model_List> dataModelArrayList = new ArrayList<>();
    private list_Data list_data;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        initviews();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            tvName.setText(getResources().getString(R.string.welcome)+firebaseUser.getDisplayName());
        }

        googleSignInClient = GoogleSignIn.getClient(ToDo.this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);

        showTask = new DatabaseHelper(this);
        txtView = findViewById(R.id.txtView);
        rv_List = findViewById(R.id.rv_List);
        rv_List.setHasFixedSize(true);

        rv_List.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(final int position)
            {
                new IOSDialog.Builder(ToDo.this)
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.are_you_sure))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String id = dataModelArrayList.get(position).getId();
                                dataModelArrayList.remove(position);
                                showTask.deleteData(Integer.parseInt(id));

//                                list_data.notifyItemRemoved(position);

                                Intent intent = new Intent(getApplicationContext(), ToDo.class);
                                startActivity(intent);
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

            @Override
            public void onSwipedRight(int position) {
                list_data.notifyDataSetChanged();
            }
        });

        showData();
    }

    private void showData()
    {
        dataModelArrayList = showTask.getdata();
        setupList();
    }

    private void setupList()
    {
        if (dataModelArrayList.size()<=0)
        {
            txtView.setVisibility(View.VISIBLE);
            rv_List.setVisibility(View.GONE);
        }
        else
        {
            txtView.setVisibility(View.GONE);
            rv_List.setVisibility(View.VISIBLE);

            list_data = new list_Data(this, dataModelArrayList);
            rv_List.setLayoutManager(new LinearLayoutManager(ToDo.this, LinearLayoutManager.VERTICAL, false));
            rv_List.setAdapter(list_data);
        }
    }


    private void initviews()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        tvName = findViewById(R.id.txtName);
        dl = (DrawerLayout) findViewById(R.id.activity_drawer);

        bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.todo);

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
                        return true;

                    case R.id.stopwatch:
                        intent = new Intent(getApplicationContext(), StopWatch.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public void logout(View view)
    {
        new IOSDialog.Builder(ToDo.this)
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
                                Toast.makeText(ToDo.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

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

    public void todoTask(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Todo_Act.class);
        intent.putExtra("id", dataModelArrayList.size());
        startActivity(intent);
    }
}
