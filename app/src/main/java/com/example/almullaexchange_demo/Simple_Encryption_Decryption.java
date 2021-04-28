package com.example.almullaexchange_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almullaexchange_demo.changeLanguage.Change_Language;
import com.example.almullaexchange_demo.stopWatch.StopWatch;
import com.example.almullaexchange_demo.toDo.ToDo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ligl.android.widget.iosdialog.IOSDialog;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Simple_Encryption_Decryption extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    TextView tvName;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    private DrawerLayout dl;
    String outputString;
    EditText et_message;
    EditText et_secretKey;
    TextView txtoutput;
    String AES = "AES";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple__encryption__decryption);

        firebaseAuth = FirebaseAuth.getInstance();
        tvName = findViewById(R.id.txtName);
        et_secretKey = findViewById(R.id.et_secretKey);
        et_message = findViewById(R.id.et_message);
        txtoutput = findViewById(R.id.txtoutput);
        dl = (DrawerLayout) findViewById(R.id.activity_drawer);

        bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.encryption);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.encryption:
                        return true;

                    case R.id.todo:
                        Intent intent = new Intent(getApplicationContext(), ToDo.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dl, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            tvName.setText(getResources().getString(R.string.welcome)+firebaseUser.getDisplayName());
        }

        googleSignInClient = GoogleSignIn.getClient(Simple_Encryption_Decryption.this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);

    }

    public void logout(View view)
    {
        new IOSDialog.Builder(Simple_Encryption_Decryption.this)
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
                                    Toast.makeText(Simple_Encryption_Decryption.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

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

    public void openNav(View view)
    {
        dl.openDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        {
            int id = item.getItemId();
            return true;
        }

    }

    public void changeLanguge(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Change_Language.class);
        startActivity(intent);
    }

    public void encryptText(View view)
    {
//        Toast.makeText(this, "Encrypt Task", Toast.LENGTH_SHORT).show();
        String Data =  et_message.getText().toString();
        String password = et_secretKey.getText().toString();

        if (Data.length()<1)
        {
            Toast.makeText(this, "Enter Message to Encrypt", Toast.LENGTH_SHORT).show();
        }
        else if (password.length()<1)
        {
            Toast.makeText(this, "Enter Password to Encrypt Data!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            try
            {
                SecretKeySpec secretKeySpec = generatekey(password);
                Cipher cipher = Cipher.getInstance(AES);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                byte[] bytes = cipher.doFinal(Data.getBytes());
                String encryptVal = Base64.encodeToString(bytes, Base64.DEFAULT);
                outputString = encryptVal;
                txtoutput.setText(outputString);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private SecretKeySpec generatekey(String password) throws Exception
    {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return  secretKeySpec;
    }

    public void decryptText(View view)
    {
//        Toast.makeText(this, "Decrypt text", Toast.LENGTH_SHORT).show();

        String Data =  et_message.getText().toString();
        String password = et_secretKey.getText().toString();

        if (Data.length()<1)
        {
            Toast.makeText(this, "Enter Message to Decrypt", Toast.LENGTH_SHORT).show();
        }
        else if (password.length()<1)
        {
            Toast.makeText(this, "Enter Password to Decrypt Data!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            outputString = decrypttext(outputString, et_secretKey.getText().toString());
            txtoutput.setText(outputString);
        }
    }

    private String decrypttext(String outputString, String password)
    {
        try
        {
            SecretKeySpec secretKeySpec = generatekey(password);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] bytes = Base64.decode(outputString, Base64.DEFAULT);
            byte[] decVal = cipher.doFinal(bytes);
            String decryptVal = new String(decVal);
            return decryptVal;
        }
        catch (Exception e)
        {
            Log.e("ASA_Decrypt: ", e.getMessage());
        }
        return null;
    }
}
