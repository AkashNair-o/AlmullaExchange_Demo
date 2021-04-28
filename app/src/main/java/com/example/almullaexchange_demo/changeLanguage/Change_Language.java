package com.example.almullaexchange_demo.changeLanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.almullaexchange_demo.MainActivity;
import com.example.almullaexchange_demo.R;

import java.util.Locale;

public class Change_Language extends AppCompatActivity
{
    private RadioGroup lang;
    private RadioButton radioEng, radioArabic;
    private RadioButton languageSelected;
    Button changeLang;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__language);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        lang = (RadioGroup) findViewById(R.id.lang);
        changeLang = (Button) findViewById(R.id.changeLang);

        changeLang.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int selectedId = lang.getCheckedRadioButtonId();
                if (selectedId == R.id.radioEng) {
                    setAppLocale(CommonEnvironmentValues.ENGLISH);
                } else if (selectedId == R.id.radioArabic)
                {
                    setAppLocale(CommonEnvironmentValues.ARABIC);
                }
                /*int selectedId = lang.getCheckedRadioButtonId();
                languageSelected = (RadioButton) findViewById(selectedId);

//                Toast.makeText(Change_Language.this, languageSelected.getText(), Toast.LENGTH_SHORT).show();
                changeLanguage(languageSelected.getText().toString());*/
            }
        });
    }

/*    private void changeLanguage(String langauge)
    {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(langauge.toLowerCase()));
        } else {
            config.locale = new Locale(langauge.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
        CommonMethodes.saveStringPreferences(Change_Language.this, CommonEnvironmentValues.SELECTED_LANGUAGE, langauge);

        finish();
    }*/

    private void setAppLocale(String localeCode) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
        CommonMethodes.saveStringPreferences(Change_Language.this, CommonEnvironmentValues.SELECTED_LANGUAGE, localeCode);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void backChnage(View view)
    {
        finish();
    }
}